package org.bouncycastle.asn1.x509.qualified

import org.bouncycastle.asn1.ASN1Choice
import org.bouncycastle.asn1.ASN1Encodable
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Object
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.asn1.DERPrintableString

/**
 * The Iso4217CurrencyCode object.
 *
 * Iso4217CurrencyCode  ::=  CHOICE {
 * alphabetic              PrintableString (SIZE 3), --Recommended
 * numeric              INTEGER (1..999) }
 * -- Alphabetic or numeric currency code as defined in ISO 4217
 * -- It is recommended that the Alphabetic form is used
 *
 */
class Iso4217CurrencyCode : ASN1Object, ASN1Choice {
    internal val ALPHABETIC_MAXSIZE = 3
    internal val NUMERIC_MINSIZE = 1
    internal val NUMERIC_MAXSIZE = 999

    internal var obj: ASN1Encodable
    internal var numeric: Int = 0

    constructor(
            numeric: Int) {
        if (numeric > NUMERIC_MAXSIZE || numeric < NUMERIC_MINSIZE) {
            throw IllegalArgumentException("wrong size in numeric code : not in ($NUMERIC_MINSIZE..$NUMERIC_MAXSIZE)")
        }
        obj = ASN1Integer(numeric.toLong())
    }

    constructor(
            alphabetic: String) {
        if (alphabetic.length > ALPHABETIC_MAXSIZE) {
            throw IllegalArgumentException("wrong size in alphabetic code : max size is " + ALPHABETIC_MAXSIZE)
        }
        obj = DERPrintableString(alphabetic)
    }

    val isAlphabetic: Boolean
        get() = obj is DERPrintableString

    val alphabetic: String
        get() = (obj as DERPrintableString).string

    fun getNumeric(): Int {
        return (obj as ASN1Integer).value.toInt()
    }

    override fun toASN1Primitive(): ASN1Primitive {
        return obj.toASN1Primitive()
    }

    companion object {

        fun getInstance(
                obj: Any?): Iso4217CurrencyCode {
            if (obj == null || obj is Iso4217CurrencyCode) {
                return obj as Iso4217CurrencyCode?
            }

            if (obj is ASN1Integer) {
                val numericobj = ASN1Integer.getInstance(obj)
                val numeric = numericobj.value.toInt()
                return Iso4217CurrencyCode(numeric)
            } else if (obj is DERPrintableString) {
                val alphabetic = DERPrintableString.getInstance(obj)
                return Iso4217CurrencyCode(alphabetic.string)
            }
            throw IllegalArgumentException("unknown object in getInstance")
        }
    }
}
