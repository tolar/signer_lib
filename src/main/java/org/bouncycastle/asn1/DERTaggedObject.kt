package org.bouncycastle.asn1

import java.io.IOException

/**
 * DER TaggedObject - in ASN.1 notation this is any object preceded by
 * a [n] where n is some number - these are assumed to follow the construction
 * rules (as with sequences).
 */
class DERTaggedObject : ASN1TaggedObject {

    /**
     * @param explicit true if an explicitly tagged object.
     * *
     * @param tagNo the tag number for this object.
     * *
     * @param obj the tagged object.
     */
    constructor(
            explicit: Boolean,
            tagNo: Int,
            obj: ASN1Encodable) : super(explicit, tagNo, obj) {
    }

    constructor(tagNo: Int, encodable: ASN1Encodable) : super(true, tagNo, encodable) {
    }

    internal override val isConstructed: Boolean
        get() {
            if (!isEmpty) {
                if (isExplicit) {
                    return true
                } else {
                    val primitive = obj!!.toASN1Primitive().toDERObject()

                    return primitive.isConstructed
                }
            } else {
                return true
            }
        }

    @Throws(IOException::class)
    internal override fun encodedLength(): Int {
        if (!isEmpty) {
            val primitive = obj!!.toASN1Primitive().toDERObject()
            var length = primitive.encodedLength()

            if (isExplicit) {
                return StreamUtil.calculateTagLength(tagNo) + StreamUtil.calculateBodyLength(length) + length
            } else {
                // header length already in calculation
                length = length - 1

                return StreamUtil.calculateTagLength(tagNo) + length
            }
        } else {
            return StreamUtil.calculateTagLength(tagNo) + 1
        }
    }

    @Throws(IOException::class)
    internal override fun encode(
            out: ASN1OutputStream) {
        if (!isEmpty) {
            val primitive = obj!!.toASN1Primitive().toDERObject()

            if (isExplicit) {
                out.writeTag(BERTags.CONSTRUCTED or BERTags.TAGGED, tagNo)
                out.writeLength(primitive.encodedLength())
                out.writeObject(primitive)
            } else {
                //
                // need to mark constructed types...
                //
                val flags: Int
                if (primitive.isConstructed) {
                    flags = BERTags.CONSTRUCTED or BERTags.TAGGED
                } else {
                    flags = BERTags.TAGGED
                }

                out.writeTag(flags, tagNo)
                out.writeImplicitObject(primitive)
            }
        } else {
            out.writeEncoded(BERTags.CONSTRUCTED or BERTags.TAGGED, tagNo, ZERO_BYTES)
        }
    }

    companion object {
        private val ZERO_BYTES = ByteArray(0)
    }
}
