/*
 * $Id: 4bf30120b879994fb19f967be1b33960ecdeead9 $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2016 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.security

import java.io.IOException
import java.net.URL
import java.security.GeneralSecurityException
import java.security.cert.CertificateFactory
import java.security.cert.X509CRL
import java.security.cert.X509Certificate
import java.util.ArrayList
import java.util.Date
import java.util.Enumeration

import com.itextpdf.text.log.Logger
import com.itextpdf.text.log.LoggerFactory

/**
 * Class that allows you to verify a certificate against
 * one or more Certificate Revocation Lists.
 */
class CRLVerifier
/**
 * Creates a CRLVerifier instance.
 * @param verifier    the next verifier in the chain
 * *
 * @param crls a list of CRLs
 */
(verifier: CertificateVerifier,
 /** The list of CRLs to check for revocation date.  */
 internal var crls: List<X509CRL>?) : RootStoreVerifier(verifier) {

    /**
     * Verifies if a a valid CRL is found for the certificate.
     * If this method returns false, it doesn't mean the certificate isn't valid.
     * It means we couldn't verify it against any CRL that was available.
     * @param signCert    the certificate that needs to be checked
     * *
     * @param issuerCert    its issuer
     * *
     * @return a list of `VerificationOK` objects.
     * * The list will be empty if the certificate couldn't be verified.
     * *
     * @see com.itextpdf.text.pdf.security.RootStoreVerifier.verify
     */
    @Throws(GeneralSecurityException::class, IOException::class)
    override fun verify(signCert: X509Certificate, issuerCert: X509Certificate?, signDate: Date?): List<VerificationOK> {
        val result = ArrayList<VerificationOK>()
        var validCrlsFound = 0
        // first check the list of CRLs that is provided
        if (crls != null) {
            for (crl in crls!!) {
                if (verify(crl, signCert, issuerCert, signDate))
                    validCrlsFound++
            }
        }
        // then check online if allowed
        var online = false
        if (onlineCheckingAllowed && validCrlsFound == 0) {
            if (verify(getCRL(signCert, issuerCert), signCert, issuerCert, signDate)) {
                validCrlsFound++
                online = true
            }
        }
        // show how many valid CRLs were found
        LOGGER.info("Valid CRLs found: " + validCrlsFound)
        if (validCrlsFound > 0) {
            result.add(VerificationOK(signCert, this.javaClass, "Valid CRLs found: " + validCrlsFound + if (online) " (online)" else ""))
        }
        if (verifier != null)
            result.addAll(verifier!!.verify(signCert, issuerCert, signDate))
        // verify using the previous verifier in the chain (if any)
        return result
    }

    /**
     * Verifies a certificate against a single CRL.
     * @param crl    the Certificate Revocation List
     * *
     * @param signCert    a certificate that needs to be verified
     * *
     * @param issuerCert    its issuer
     * *
     * @param signDate        the sign date
     * *
     * @return true if the verification succeeded
     * *
     * @throws GeneralSecurityException
     */
    @Throws(GeneralSecurityException::class)
    fun verify(crl: X509CRL?, signCert: X509Certificate, issuerCert: X509Certificate, signDate: Date?): Boolean {
        if (crl == null || signDate == null)
            return false
        // We only check CRLs valid on the signing date for which the issuer matches
        if (crl.issuerX500Principal == signCert.issuerX500Principal
                && signDate.after(crl.thisUpdate) && signDate.before(crl.nextUpdate)) {
            // the signing certificate may not be revoked
            if (isSignatureValid(crl, issuerCert) && crl.isRevoked(signCert)) {
                throw VerificationException(signCert, "The certificate has been revoked.")
            }
            return true
        }
        return false
    }

    /**
     * Fetches a CRL for a specific certificate online (without further checking).
     * @param signCert    the certificate
     * *
     * @param issuerCert    its issuer
     * *
     * @return    an X509CRL object
     */
    fun getCRL(signCert: X509Certificate, issuerCert: X509Certificate?): X509CRL? {
        var issuerCert = issuerCert
        if (issuerCert == null)
            issuerCert = signCert
        try {
            // gets the URL from the certificate
            val crlurl = CertificateUtil.getCRLURL(signCert) ?: return null
            LOGGER.info("Getting CRL from " + crlurl)
            val cf = CertificateFactory.getInstance("X.509")
            // Creates the CRL
            return cf.generateCRL(URL(crlurl).openStream()) as X509CRL
        } catch (e: IOException) {
            return null
        } catch (e: GeneralSecurityException) {
            return null
        }

    }

    /**
     * Checks if a CRL verifies against the issuer certificate or a trusted anchor.
     * @param crl    the CRL
     * *
     * @param crlIssuer    the trusted anchor
     * *
     * @return    true if the CRL can be trusted
     */
    fun isSignatureValid(crl: X509CRL, crlIssuer: X509Certificate?): Boolean {
        // check if the CRL was issued by the issuer
        if (crlIssuer != null) {
            try {
                crl.verify(crlIssuer.publicKey)
                return true
            } catch (e: GeneralSecurityException) {
                LOGGER.warn("CRL not issued by the same authority as the certificate that is being checked")
            }

        }
        // check the CRL against trusted anchors
        if (rootStore == null)
            return false
        try {
            // loop over the certificate in the key store
            val aliases = rootStore!!.aliases()
            while (aliases.hasMoreElements()) {
                val alias = aliases.nextElement()
                try {
                    if (!rootStore!!.isCertificateEntry(alias))
                        continue
                    // check if the crl was signed by a trusted party (indirect CRLs)
                    val anchor = rootStore!!.getCertificate(alias) as X509Certificate
                    crl.verify(anchor.publicKey)
                    return true
                } catch (e: GeneralSecurityException) {
                    continue
                }

            }
        } catch (e: GeneralSecurityException) {
            return false
        }

        return false
    }

    companion object {

        /** The Logger instance  */
        protected val LOGGER = LoggerFactory.getLogger(CRLVerifier::class.java)
    }
}
