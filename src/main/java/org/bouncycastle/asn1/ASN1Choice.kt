package org.bouncycastle.asn1

/**
 * Marker interface for CHOICE objects - if you implement this in a role your
 * own object any attempt to tag the object implicitly will convert the tag to
 * an explicit one as the encoding rules require.
 *
 *
 * If you use this interface your class should also implement the getInstance()
 * pattern which takes a tag object and the tagging mode used.
 *
 *
 *
 * **X.690**
 *
 * **8: Basic encoding rules**
 *
 * **8.13 Encoding of a choice value **
 *
 *
 * The encoding of a choice value shall be the same as the encoding of a value of the chosen type.
 *
 * NOTE 1  The encoding may be primitive or constructed depending on the chosen type.
 *
 * NOTE 2  The tag used in the identifier octets is the tag of the chosen type,
 * as specified in the ASN.1 definition of the choice type.
 *
 *
 */
interface ASN1Choice// marker interface
