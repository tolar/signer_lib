package org.bouncycastle.asn1

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

import org.bouncycastle.util.Arrays
import org.bouncycastle.util.encoders.Hex

/**
 * Abstract base for the ASN.1 OCTET STRING data type
 *
 *
 * This supports BER, and DER forms of the data.
 *
 *
 * DER form is always primitive single OCTET STRING, while
 * BER support includes the constructed forms.
 *
 *
 *
 * **X.690**
 *
 * **8: Basic encoding rules**
 *
 * **8.7 Encoding of an octetstring value**
 *
 *
 * **8.7.1** The encoding of an octetstring value shall be
 * either primitive or constructed at the option of the sender.
 *
 * NOTE  Where it is necessary to transfer part of an octet string
 * before the entire OCTET STRING is available, the constructed encoding
 * is used.
 *
 *
 *
 * **8.7.2** The primitive encoding contains zero,
 * one or more contents octets equal in value to the octets
 * in the data value, in the order they appear in the data value,
 * and with the most significant bit of an octet of the data value
 * aligned with the most significant bit of an octet of the contents octets.
 *
 *
 *
 * **8.7.3** The contents octets for the constructed encoding shall consist
 * of zero, one, or more encodings.
 *
 * NOTE  Each such encoding includes identifier, length, and contents octets,
 * and may include end-of-contents octets if it is constructed.
 *
 *
 *
 *
 * **8.7.3.1** To encode an octetstring value in this way,
 * it is segmented. Each segment shall consist of a series of
 * consecutive octets of the value. There shall be no significance
 * placed on the segment boundaries.
 *
 * NOTE  A segment may be of size zero, i.e. contain no octets.
 *
 *
 *
 *
 * **8.7.3.2** Each encoding in the contents octets shall represent
 * a segment of the overall octetstring, the encoding arising from
 * a recursive application of this subclause.
 * In this recursive application, each segment is treated as if it were
 * a octetstring value. The encodings of the segments shall appear in the contents
 * octets in the order in which their octets appear in the overall value.
 *
 * NOTE 1  As a consequence of this recursion,
 * each encoding in the contents octets may itself
 * be primitive or constructed.
 * However, such encodings will usually be primitive.
 *
 * NOTE 2  In particular, the tags in the contents octets are always universal class, number 4.
 *
 *
 *
 * **9: Canonical encoding rules**
 *
 * **9.1 Length forms**
 *
 *
 * If the encoding is constructed, it shall employ the indefinite-length form.
 * If the encoding is primitive, it shall include the fewest length octets necessary.
 * [Contrast with 8.1.3.2 b).]
 *
 *
 * **9.2 String encoding forms**
 *
 *
 * BIT STRING, OCTET STRING,and restricted character string
 * values shall be encoded with a primitive encoding if they would
 * require no more than 1000 contents octets, and as a constructed
 * encoding otherwise. The string fragments contained in
 * the constructed encoding shall be encoded with a primitive encoding.
 * The encoding of each fragment, except possibly
 * the last, shall have 1000 contents octets. (Contrast with 8.21.6.)
 *
 *
 * **10: Distinguished encoding rules**
 *
 *
 * **10.1 Length forms**
 * The definite form of length encoding shall be used,
 * encoded in the minimum number of octets.
 * [Contrast with 8.1.3.2 b).]
 *
 *
 * **10.2 String encoding forms**
 * For BIT STRING, OCTET STRING and restricted character string types,
 * the constructed form of encoding shall not be used.
 * (Contrast with 8.21.6.)
 *
 */
abstract class ASN1OctetString
/**
 * Base constructor.

 * @param string the octets making up the octet string.
 */
(
        string: ByteArray?) : ASN1Primitive(), ASN1OctetStringParser {
    /**
     * Return the content of the OCTET STRING as a byte array.

     * @return the byte[] representing the OCTET STRING's content.
     */
    var octets: ByteArray
        internal set

    init {
        if (string == null) {
            throw NullPointerException("string cannot be null")
        }
        this.octets = string
    }

    /**
     * Return the content of the OCTET STRING as an InputStream.

     * @return an InputStream representing the OCTET STRING's content.
     */
    override val octetStream: InputStream
        get() = ByteArrayInputStream(octets)

    /**
     * Return the parser associated with this object.

     * @return a parser based on this OCTET STRING
     */
    fun parser(): ASN1OctetStringParser {
        return this
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(this.octets)
    }

    internal override fun asn1Equals(
            o: ASN1Primitive): Boolean {
        if (o !is ASN1OctetString) {
            return false
        }

        return Arrays.areEqual(octets, o.octets)
    }

    override val loadedObject: ASN1Primitive
        get() = this.toASN1Primitive()

    internal override fun toDERObject(): ASN1Primitive {
        return DEROctetString(octets)
    }

    internal override fun toDLObject(): ASN1Primitive {
        return DEROctetString(octets)
    }

    @Throws(IOException::class)
    internal abstract override fun encode(out: ASN1OutputStream)

    override fun toString(): String {
        return "#" + String(Hex.encode(octets))
    }

    companion object {

        /**
         * return an Octet String from a tagged object.

         * @param obj the tagged object holding the object we want.
         * *
         * @param explicit true if the object is meant to be explicitly
         * *              tagged false otherwise.
         * *
         * @exception IllegalArgumentException if the tagged object cannot
         * *              be converted.
         */
        fun getInstance(
                obj: ASN1TaggedObject,
                explicit: Boolean): ASN1OctetString {
            val o = obj.`object`

            if (explicit || o is ASN1OctetString) {
                return getInstance(o)
            } else {
                return BEROctetString.fromSequence(ASN1Sequence.getInstance(o))
            }
        }

        /**
         * return an Octet String from the given object.

         * @param obj the object we want converted.
         * *
         * @exception IllegalArgumentException if the object cannot be converted.
         */
        fun getInstance(
                obj: Any?): ASN1OctetString {
            if (obj == null || obj is ASN1OctetString) {
                return obj as ASN1OctetString?
            } else if (obj is ByteArray) {
                try {
                    return ASN1OctetString.getInstance(ASN1Primitive.fromByteArray(obj as ByteArray?))
                } catch (e: IOException) {
                    throw IllegalArgumentException("failed to construct OCTET STRING from byte[]: " + e.message)
                }

            } else if (obj is ASN1Encodable) {
                val primitive = obj.toASN1Primitive()

                if (primitive is ASN1OctetString) {
                    return primitive
                }
            }

            throw IllegalArgumentException("illegal object in getInstance: " + obj.javaClass.name)
        }
    }
}
