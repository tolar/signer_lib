package org.bouncycastle.asn1.isismtt

import org.bouncycastle.asn1.ASN1ObjectIdentifier

/**
 * ISISMT -- Industrial Signature Interoperability Specification
 */
interface ISISMTTObjectIdentifiers {
    companion object {

        /** 1.3.36.8  */
        val id_isismtt = ASN1ObjectIdentifier("1.3.36.8")

        /** 1.3.36.8.1  */
        val id_isismtt_cp = id_isismtt.branch("1")

        /**
         * The id-isismtt-cp-accredited OID indicates that the certificate is a
         * qualified certificate according to Directive 1999/93/EC of the European
         * Parliament and of the Council of 13 December 1999 on a Community
         * Framework for Electronic Signatures, which additionally conforms the
         * special requirements of the SigG and has been issued by an accredited CA.
         *
         *
         * 1.3.36.8.1.1
         */

        val id_isismtt_cp_accredited = id_isismtt_cp.branch("1")

        /** 1.3.36.8.3  */
        val id_isismtt_at = id_isismtt.branch("3")

        /**
         * Certificate extensionDate of certificate generation
         *
         * DateOfCertGenSyntax ::= GeneralizedTime
         *
         * OID: 1.3.36.8.3.1
         */
        val id_isismtt_at_dateOfCertGen = id_isismtt_at.branch("1")

        /**
         * Attribute to indicate that the certificate holder may sign in the name of
         * a third person. May also be used as extension in a certificate.
         *
         *
         * OID: 1.3.36.8.3.2
         */
        val id_isismtt_at_procuration = id_isismtt_at.branch("2")

        /**
         * Attribute to indicate admissions to certain professions. May be used as
         * attribute in attribute certificate or as extension in a certificate
         *
         *
         * OID: 1.3.36.8.3.3
         */
        val id_isismtt_at_admission = id_isismtt_at.branch("3")

        /**
         * Monetary limit for transactions. The QcEuMonetaryLimit QC statement MUST
         * be used in new certificates in place of the extension/attribute
         * MonetaryLimit since January 1, 2004. For the sake of backward
         * compatibility with certificates already in use, SigG conforming
         * components MUST support MonetaryLimit (as well as QcEuLimitValue).
         *
         *
         * OID: 1.3.36.8.3.4
         */
        val id_isismtt_at_monetaryLimit = id_isismtt_at.branch("4")

        /**
         * A declaration of majority. May be used as attribute in attribute
         * certificate or as extension in a certificate
         *
         *
         * OID: 1.3.36.8.3.5
         */
        val id_isismtt_at_declarationOfMajority = id_isismtt_at.branch("5")

        /**
         * Serial number of the smart card containing the corresponding private key
         *
         * ICCSNSyntax ::= OCTET STRING (SIZE(8..20))
         *
         *
         *
         * OID: 1.3.36.8.3.6
         */
        val id_isismtt_at_iCCSN = id_isismtt_at.branch("6")

        /**
         * Reference for a file of a smartcard that stores the public key of this
         * certificate and that is used as "security anchor".
         *
         * PKReferenceSyntax ::= OCTET STRING (SIZE(20))
         *
         *
         *
         * OID: 1.3.36.8.3.7
         */
        val id_isismtt_at_PKReference = id_isismtt_at.branch("7")

        /**
         * Some other restriction regarding the usage of this certificate. May be
         * used as attribute in attribute certificate or as extension in a
         * certificate.
         *
         * RestrictionSyntax ::= DirectoryString (SIZE(1..1024))
         *
         *
         *
         * OID: 1.3.36.8.3.8

         * @see org.bouncycastle.asn1.isismtt.x509.Restriction
         */
        val id_isismtt_at_restriction = id_isismtt_at.branch("8")

        /**
         * (Single)Request extension: Clients may include this extension in a
         * (single) Request to request the responder to send the certificate in the
         * response message along with the status information. Besides the LDAP
         * service, this extension provides another mechanism for the distribution
         * of certificates, which MAY optionally be provided by certificate
         * repositories.
         *
         * RetrieveIfAllowed ::= BOOLEAN
         *
         *
         *
         * OID: 1.3.36.8.3.9
         */
        val id_isismtt_at_retrieveIfAllowed = id_isismtt_at.branch("9")

        /**
         * SingleOCSPResponse extension: The certificate requested by the client by
         * inserting the RetrieveIfAllowed extension in the request, will be
         * returned in this extension.
         *
         *
         * OID: 1.3.36.8.3.10

         * @see org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate
         */
        val id_isismtt_at_requestedCertificate = id_isismtt_at.branch("10")

        /**
         * Base ObjectIdentifier for naming authorities
         *
         *
         * OID: 1.3.36.8.3.11
         */
        val id_isismtt_at_namingAuthorities = id_isismtt_at.branch("11")

        /**
         * SingleOCSPResponse extension: Date, when certificate has been published
         * in the directory and status information has become available. Currently,
         * accrediting authorities enforce that SigG-conforming OCSP servers include
         * this extension in the responses.

         *
         * CertInDirSince ::= GeneralizedTime
         *
         *
         *
         * OID: 1.3.36.8.3.12
         */
        val id_isismtt_at_certInDirSince = id_isismtt_at.branch("12")

        /**
         * Hash of a certificate in OCSP.
         *
         *
         * OID: 1.3.36.8.3.13

         * @see org.bouncycastle.asn1.isismtt.ocsp.CertHash
         */
        val id_isismtt_at_certHash = id_isismtt_at.branch("13")

        /**
         *
         * NameAtBirth ::= DirectoryString(SIZE(1..64)
         *

         * Used in
         * [SubjectDirectoryAttributes][org.bouncycastle.asn1.x509.SubjectDirectoryAttributes]
         *
         *
         * OID: 1.3.36.8.3.14
         */
        val id_isismtt_at_nameAtBirth = id_isismtt_at.branch("14")

        /**
         * Some other information of non-restrictive nature regarding the usage of
         * this certificate. May be used as attribute in atribute certificate or as
         * extension in a certificate.

         *
         * AdditionalInformationSyntax ::= DirectoryString (SIZE(1..2048))
         *
         *
         *
         * OID: 1.3.36.8.3.15

         * @see org.bouncycastle.asn1.isismtt.x509.AdditionalInformationSyntax
         */
        val id_isismtt_at_additionalInformation = id_isismtt_at.branch("15")

        /**
         * Indicates that an attribute certificate exists, which limits the
         * usability of this public key certificate. Whenever verifying a signature
         * with the help of this certificate, the content of the corresponding
         * attribute certificate should be concerned. This extension MUST be
         * included in a PKC, if a corresponding attribute certificate (having the
         * PKC as base certificate) contains some attribute that restricts the
         * usability of the PKC too. Attribute certificates with restricting content
         * MUST always be included in the signed document.
         *
         * LiabilityLimitationFlagSyntax ::= BOOLEAN
         *
         *
         *
         * OID: 0.2.262.1.10.12.0
         */
        val id_isismtt_at_liabilityLimitationFlag = ASN1ObjectIdentifier("0.2.262.1.10.12.0")
    }
}
