package org.bouncycastle.asn1.test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Hashtable
import java.util.Vector

import org.bouncycastle.asn1.ASN1Encodable
import org.bouncycastle.asn1.ASN1EncodableVector
import org.bouncycastle.asn1.ASN1GeneralizedTime
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.ASN1OutputStream
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.ASN1Set
import org.bouncycastle.asn1.DERIA5String
import org.bouncycastle.asn1.DERPrintableString
import org.bouncycastle.asn1.DERSequence
import org.bouncycastle.asn1.DERSet
import org.bouncycastle.asn1.DERUTF8String
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.X509DefaultEntryConverter
import org.bouncycastle.asn1.x509.X509Name
import org.bouncycastle.util.Arrays
import org.bouncycastle.util.encoders.Hex
import org.bouncycastle.util.test.SimpleTest

class X509NameTest : SimpleTest() {
    internal var subjects = arrayOf("C=AU,ST=Victoria,L=South Melbourne,O=Connect 4 Pty Ltd,OU=Webserver Team,CN=www2.connect4.com.au,E=webmaster@connect4.com.au", "C=AU,ST=Victoria,L=South Melbourne,O=Connect 4 Pty Ltd,OU=Certificate Authority,CN=Connect 4 CA,E=webmaster@connect4.com.au", "C=AU,ST=QLD,CN=SSLeay/rsa test cert", "C=US,O=National Aeronautics and Space Administration,SERIALNUMBER=16+CN=Steve Schoch", "E=cooke@issl.atl.hp.com,C=US,OU=Hewlett Packard Company (ISSL),CN=Paul A. Cooke", "O=Sun Microsystems Inc,CN=store.sun.com", "unstructuredAddress=192.168.1.33,unstructuredName=pixfirewall.ciscopix.com,CN=pixfirewall.ciscopix.com", "CN=*.canal-plus.com,OU=Provided by TBS INTERNET http://www.tbs-certificats.com/,OU=\\ CANAL \\+,O=CANAL\\+DISTRIBUTION,L=issy les moulineaux,ST=Hauts de Seine,C=FR", "O=Bouncy Castle,CN=www.bouncycastle.org\\ ", "O=Bouncy Castle,CN=c:\\\\fred\\\\bob")

    override fun getName(): String {
        return "X509Name"
    }

    private fun createEntryValue(oid: ASN1ObjectIdentifier, value: String): ASN1Encodable {
        val attrs = Hashtable()

        attrs.put(oid, value)

        val order = Vector()

        order.addElement(oid)

        val name = X509Name(order, attrs)

        val set = name.toASN1Primitive().getObjectAt(0) as ASN1Set
        name.toASN1Primitive() = set.getObjectAt(0) as ASN1Sequence

        return name.toASN1Primitive().getObjectAt(1)
    }

    private fun createEntryValueFromString(oid: ASN1ObjectIdentifier, value: String): ASN1Encodable {
        val attrs = Hashtable()

        attrs.put(oid, value)

        val order = Vector()

        order.addElement(oid)

        val name = X509Name(X509Name(order, attrs).toString())

        val set = name.toASN1Primitive().getObjectAt(0) as ASN1Set
        name.toASN1Primitive() = set.getObjectAt(0) as ASN1Sequence

        return name.toASN1Primitive().getObjectAt(1)
    }

    private fun testEncodingPrintableString(oid: ASN1ObjectIdentifier, value: String) {
        val converted = createEntryValue(oid, value)
        if (converted !is DERPrintableString) {
            fail("encoding for $oid not printable string")
        }
    }

    private fun testEncodingIA5String(oid: ASN1ObjectIdentifier, value: String) {
        val converted = createEntryValue(oid, value)
        if (converted !is DERIA5String) {
            fail("encoding for $oid not IA5String")
        }
    }


    @Throws(IOException::class)
    private fun testEncodingUTF8String(oid: ASN1ObjectIdentifier, value: String) {
        val converted = createEntryValue(oid, value)
        if (converted !is DERUTF8String) {
            fail("encoding for $oid not IA5String")
        }
        if (value != DERUTF8String.getInstance(converted.toASN1Primitive().encoded).string) {
            fail("decoding not correct")
        }
    }

    private fun testEncodingGeneralizedTime(oid: ASN1ObjectIdentifier, value: String) {
        var converted = createEntryValue(oid, value)
        if (converted !is ASN1GeneralizedTime) {
            fail("encoding for $oid not GeneralizedTime")
        }
        converted = createEntryValueFromString(oid, value)
        if (converted !is ASN1GeneralizedTime) {
            fail("encoding for $oid not GeneralizedTime")
        }
    }

    @Throws(Exception::class)
    override fun performTest() {
        testEncodingPrintableString(X509Name.C, "AU")
        testEncodingPrintableString(X509Name.SERIALNUMBER, "123456")
        testEncodingPrintableString(X509Name.DN_QUALIFIER, "123456")
        testEncodingIA5String(X509Name.EmailAddress, "test@test.com")
        testEncodingIA5String(X509Name.DC, "test")
        // correct encoding
        testEncodingGeneralizedTime(X509Name.DATE_OF_BIRTH, "#180F32303032303132323132323232305A")
        // compatibility encoding
        testEncodingGeneralizedTime(X509Name.DATE_OF_BIRTH, "20020122122220Z")
        testEncodingUTF8String(X509Name.CN, "Mörsky")
        //
        // composite
        //
        val attrs = Hashtable()

        attrs.put(X509Name.C, "AU")
        attrs.put(X509Name.O, "The Legion of the Bouncy Castle")
        attrs.put(X509Name.L, "Melbourne")
        attrs.put(X509Name.ST, "Victoria")
        attrs.put(X509Name.E, "feedback-crypto@bouncycastle.org")

        val order = Vector()

        order.addElement(X509Name.C)
        order.addElement(X509Name.O)
        order.addElement(X509Name.L)
        order.addElement(X509Name.ST)
        order.addElement(X509Name.E)

        var name1 = X509Name(order, attrs)

        if (name1 != name1) {
            fail("Failed same object test")
        }

        if (!name1.equals(name1, true)) {
            fail("Failed same object test - in Order")
        }

        var name2 = X509Name(order, attrs)

        if (name1 != name2) {
            fail("Failed same name test")
        }

        if (!name1.equals(name2, true)) {
            fail("Failed same name test - in Order")
        }

        if (name1.hashCode() != name2.hashCode()) {
            fail("Failed same name test - in Order")
        }

        val ord1 = Vector()

        ord1.addElement(X509Name.C)
        ord1.addElement(X509Name.O)
        ord1.addElement(X509Name.L)
        ord1.addElement(X509Name.ST)
        ord1.addElement(X509Name.E)

        var ord2 = Vector()

        ord2.addElement(X509Name.E)
        ord2.addElement(X509Name.ST)
        ord2.addElement(X509Name.L)
        ord2.addElement(X509Name.O)
        ord2.addElement(X509Name.C)

        name1 = X509Name(ord1, attrs)
        name2 = X509Name(ord2, attrs)

        if (name1 != name2) {
            fail("Failed reverse name test")
        }

        if (name1.hashCode() != name2.hashCode()) {
            fail("Failed reverse name test hashCode")
        }

        if (name1.equals(name2, true)) {
            fail("Failed reverse name test - in Order")
        }

        if (!name1.equals(name2, false)) {
            fail("Failed reverse name test - in Order false")
        }

        val oids = name1.getOIDs()
        if (!compareVectors(oids, ord1)) {
            fail("oid comparison test")
        }

        val val1 = Vector()

        val1.addElement("AU")
        val1.addElement("The Legion of the Bouncy Castle")
        val1.addElement("Melbourne")
        val1.addElement("Victoria")
        val1.addElement("feedback-crypto@bouncycastle.org")

        name1 = X509Name(ord1, val1)

        val values = name1.getValues()
        if (!compareVectors(values, val1)) {
            fail("value comparison test")
        }

        ord2 = Vector()

        ord2.addElement(X509Name.ST)
        ord2.addElement(X509Name.ST)
        ord2.addElement(X509Name.L)
        ord2.addElement(X509Name.O)
        ord2.addElement(X509Name.C)

        name1 = X509Name(ord1, attrs)
        name2 = X509Name(ord2, attrs)

        if (name1 == name2) {
            fail("Failed different name test")
        }

        ord2 = Vector()

        ord2.addElement(X509Name.ST)
        ord2.addElement(X509Name.L)
        ord2.addElement(X509Name.O)
        ord2.addElement(X509Name.C)

        name1 = X509Name(ord1, attrs)
        name2 = X509Name(ord2, attrs)

        if (name1 == name2) {
            fail("Failed subset name test")
        }

        compositeTest()

        var bOut: ByteArrayOutputStream
        var aOut: ASN1OutputStream
        var aIn: ASN1InputStream

        //
        // getValues test
        //
        val v1 = name1.getValues(X509Name.O)

        if (v1.size != 1 || v1.elementAt(0) != "The Legion of the Bouncy Castle") {
            fail("O test failed")
        }

        val v2 = name1.getValues(X509Name.L)

        if (v2.size != 1 || v2.elementAt(0) != "Melbourne") {
            fail("L test failed")
        }

        //
        // general subjects test
        //
        for (i in subjects.indices) {
            var name = X509Name(subjects[i])

            bOut = ByteArrayOutputStream()
            aOut = ASN1OutputStream(bOut)

            aOut.writeObject(name)

            aIn = ASN1InputStream(ByteArrayInputStream(bOut.toByteArray()))

            name = X509Name.getInstance(aIn.readObject())

            if (name.toString() != subjects[i]) {
                fail("failed regeneration test " + i + " got " + name.toString())
            }
        }

        //
        // sort test
        //
        var unsorted = X509Name("SERIALNUMBER=BBB + CN=AA")

        if (fromBytes(unsorted.encoded).toString() != "CN=AA+SERIALNUMBER=BBB") {
            fail("failed sort test 1")
        }

        unsorted = X509Name("CN=AA + SERIALNUMBER=BBB")

        if (fromBytes(unsorted.encoded).toString() != "CN=AA+SERIALNUMBER=BBB") {
            fail("failed sort test 2")
        }

        unsorted = X509Name("SERIALNUMBER=B + CN=AA")

        if (fromBytes(unsorted.encoded).toString() != "SERIALNUMBER=B+CN=AA") {
            fail("failed sort test 3")
        }

        unsorted = X509Name("CN=AA + SERIALNUMBER=B")

        if (fromBytes(unsorted.encoded).toString() != "SERIALNUMBER=B+CN=AA") {
            fail("failed sort test 4")
        }

        //
        // equality tests
        //
        equalityTest(X509Name("CN=The     Legion"), X509Name("CN=The Legion"))
        equalityTest(X509Name("CN=   The Legion"), X509Name("CN=The Legion"))
        equalityTest(X509Name("CN=The Legion   "), X509Name("CN=The Legion"))
        equalityTest(X509Name("CN=  The     Legion "), X509Name("CN=The Legion"))
        equalityTest(X509Name("CN=  the     legion "), X509Name("CN=The Legion"))

        // # test

        var n1 = X509Name("SERIALNUMBER=8,O=ABC,CN=ABC Class 3 CA,C=LT")
        var n2 = X509Name("2.5.4.5=8,O=ABC,CN=ABC Class 3 CA,C=LT")
        var n3 = X509Name("2.5.4.5=#130138,O=ABC,CN=ABC Class 3 CA,C=LT")

        equalityTest(n1, n2)
        equalityTest(n2, n3)
        equalityTest(n3, n1)

        n1 = X509Name(true, "2.5.4.5=#130138,CN=SSC Class 3 CA,O=UAB Skaitmeninio sertifikavimo centras,C=LT")
        n2 = X509Name(true, "SERIALNUMBER=#130138,CN=SSC Class 3 CA,O=UAB Skaitmeninio sertifikavimo centras,C=LT")
        n3 = X509Name.getInstance(ASN1Primitive.fromByteArray(Hex.decode("3063310b3009060355040613024c54312f302d060355040a1326" + "55414220536b6169746d656e696e696f20736572746966696b6176696d6f2063656e74726173311730150603550403130e53534320436c6173732033204341310a30080603550405130138")))

        equalityTest(n1, n2)
        equalityTest(n2, n3)
        equalityTest(n3, n1)

        n1 = X509Name("SERIALNUMBER=8,O=XX,CN=ABC Class 3 CA,C=LT")
        n2 = X509Name("2.5.4.5=8,O=,CN=ABC Class 3 CA,C=LT")

        if (n1 == n2) {
            fail("empty inequality check failed")
        }

        n1 = X509Name("SERIALNUMBER=8,O=,CN=ABC Class 3 CA,C=LT")
        n2 = X509Name("2.5.4.5=8,O=,CN=ABC Class 3 CA,C=LT")

        equalityTest(n1, n2)

        //
        // inequality to sequences
        //
        name1 = X509Name("CN=The Legion")

        if (name1 == DERSequence()) {
            fail("inequality test with sequence")
        }

        if (name1 == DERSequence(DERSet())) {
            fail("inequality test with sequence and set")
        }

        var v = ASN1EncodableVector()

        v.add(ASN1ObjectIdentifier("1.1"))
        v.add(ASN1ObjectIdentifier("1.1"))
        if (name1 == DERSequence(DERSet(DERSet(v)))) {
            fail("inequality test with sequence and bad set")
        }

        if (name1.equals(DERSequence(DERSet(DERSet(v))), true)) {
            fail("inequality test with sequence and bad set")
        }

        if (name1 == DERSequence(DERSet(DERSequence()))) {
            fail("inequality test with sequence and short sequence")
        }

        if (name1.equals(DERSequence(DERSet(DERSequence())), true)) {
            fail("inequality test with sequence and short sequence")
        }

        v = ASN1EncodableVector()

        v.add(ASN1ObjectIdentifier("1.1"))
        v.add(DERSequence())

        if (name1 == DERSequence(DERSet(DERSequence(v)))) {
            fail("inequality test with sequence and bad sequence")
        }

        if (name1 == null) {
            fail("inequality test with null")
        }

        if (name1.equals(null, true)) {
            fail("inequality test with null")
        }

        //
        // this is contrived but it checks sorting of sets with equal elements
        //
        unsorted = X509Name("CN=AA + CN=AA + CN=AA")

        //
        // tagging test - only works if CHOICE implemented
        //
        /*
        ASN1TaggedObject tag = new DERTaggedObject(false, 1, new X509Name("CN=AA"));

        if (!tag.isExplicit())
        {
            fail("failed to explicitly tag CHOICE object");
        }

        X509Name name = X509Name.getInstance(tag, false);

        if (!name.equals(new X509Name("CN=AA")))
        {
            fail("failed to recover tagged name");
        }
        */

        val testString = DERUTF8String("The Legion of the Bouncy Castle")
        val encodedBytes = testString.encoded
        val hexEncodedBytes = Hex.encode(encodedBytes)
        val hexEncodedString = "#" + String(hexEncodedBytes)

        var converted = X509DefaultEntryConverter().getConvertedValue(
                X509Name.L, hexEncodedString) as DERUTF8String

        if (converted != testString) {
            fail("failed X509DefaultEntryConverter test")
        }

        //
        // try escaped.
        //
        converted = X509DefaultEntryConverter().getConvertedValue(
                X509Name.L, "\\" + hexEncodedString) as DERUTF8String

        if (converted != DERUTF8String(hexEncodedString)) {
            fail("failed X509DefaultEntryConverter test got $converted expected: $hexEncodedString")
        }

        //
        // try a weird value
        //
        var n = X509Name("CN=\\#nothex#string")

        if (n.toString() != "CN=\\#nothex#string") {
            fail("# string not properly escaped.")
        }

        var vls: Vector<Any> = n.getValues(X509Name.CN)
        if (vls.size != 1 || vls.elementAt(0) != "#nothex#string") {
            fail("escaped # not reduced properly")
        }

        n = X509Name("CN=\"a+b\"")

        vls = n.getValues(X509Name.CN)
        if (vls.size != 1 || vls.elementAt(0) != "a+b") {
            fail("escaped + not reduced properly")
        }

        n = X509Name("CN=a\\+b")

        vls = n.getValues(X509Name.CN)
        if (vls.size != 1 || vls.elementAt(0) != "a+b") {
            fail("escaped + not reduced properly")
        }

        if (n.toString() != "CN=a\\+b") {
            fail("+ in string not properly escaped.")
        }

        n = X509Name("CN=a\\=b")

        vls = n.getValues(X509Name.CN)
        if (vls.size != 1 || vls.elementAt(0) != "a=b") {
            fail("escaped = not reduced properly")
        }

        if (n.toString() != "CN=a\\=b") {
            fail("= in string not properly escaped.")
        }

        n = X509Name("TELEPHONENUMBER=\"+61999999999\"")

        vls = n.getValues(X509Name.TELEPHONE_NUMBER)
        if (vls.size != 1 || vls.elementAt(0) != "+61999999999") {
            fail("telephonenumber escaped + not reduced properly")
        }

        n = X509Name("TELEPHONENUMBER=\\+61999999999")

        vls = n.getValues(X509Name.TELEPHONE_NUMBER)
        if (vls.size != 1 || vls.elementAt(0) != "+61999999999") {
            fail("telephonenumber escaped + not reduced properly")
        }

        // migration
        val builder = X500NameBuilder(BCStyle.INSTANCE)
        builder.addMultiValuedRDN(arrayOf(BCStyle.CN, BCStyle.SN), arrayOf("Thomas", "CVR:12341233-UID:1111"))
        builder.addRDN(BCStyle.O, "Test")
        builder.addRDN(BCStyle.C, "DK")

        val subject = builder.build()
        val derObject = subject.toASN1Primitive()
        val instance = X509Name.getInstance(derObject)
    }

    private fun compareVectors(a: Vector<Any>, b: Vector<Any>    // for compatibility with early JDKs
    ): Boolean {
        if (a.size != b.size) {
            return false
        }

        for (i in a.indices) {
            if (a.elementAt(i) != b.elementAt(i)) {
                return false
            }
        }

        return true
    }

    @Throws(IOException::class)
    private fun compositeTest() {
        //
        // composite test
        //
        val enc = Hex.decode("305e310b300906035504061302415531283026060355040a0c1f546865204c6567696f6e206f662074686520426f756e637920436173746c653125301006035504070c094d656c626f75726e653011060355040b0c0a4173636f742056616c65")
        val aIn = ASN1InputStream(ByteArrayInputStream(enc))

        var n = X509Name.getInstance(aIn.readObject())

        if (n.toString() != "C=AU,O=The Legion of the Bouncy Castle,L=Melbourne+OU=Ascot Vale") {
            fail("Failed composite to string test got: " + n.toString())
        }

        if (n.toString(true, X509Name.DefaultSymbols) != "L=Melbourne+OU=Ascot Vale,O=The Legion of the Bouncy Castle,C=AU") {
            fail("Failed composite to string test got: " + n.toString(true, X509Name.DefaultSymbols))
        }

        n = X509Name(true, "L=Melbourne+OU=Ascot Vale,O=The Legion of the Bouncy Castle,C=AU")
        if (n.toString() != "C=AU,O=The Legion of the Bouncy Castle,L=Melbourne+OU=Ascot Vale") {
            fail("Failed composite to string reversal test got: " + n.toString())
        }

        n = X509Name("C=AU, O=The Legion of the Bouncy Castle, L=Melbourne + OU=Ascot Vale")

        val bOut = ByteArrayOutputStream()
        val aOut = ASN1OutputStream(bOut)

        aOut.writeObject(n)

        val enc2 = bOut.toByteArray()

        if (!Arrays.areEqual(enc, enc2)) {
            //fail("Failed composite string to encoding test");
        }

        //
        // dud name test - handle empty DN without barfing.
        //
        n = X509Name("C=CH,O=,OU=dummy,CN=mail@dummy.com")

        n = X509Name.getInstance(ASN1Primitive.fromByteArray(n.encoded))
    }

    private fun equalityTest(x509Name: X509Name, x509Name1: X509Name) {
        if (x509Name != x509Name1) {
            fail("equality test failed for $x509Name : $x509Name1")
        }

        if (x509Name.hashCode() != x509Name1.hashCode()) {
            fail("hashCodeTest test failed for $x509Name : $x509Name1")
        }

        if (!x509Name.equals(x509Name1, true)) {
            fail("equality test failed for $x509Name : $x509Name1")
        }
    }

    companion object {

        @Throws(IOException::class)
        private fun fromBytes(
                bytes: ByteArray): X509Name {
            return X509Name.getInstance(ASN1InputStream(ByteArrayInputStream(bytes)).readObject())
        }


        @JvmStatic fun main(
                args: Array<String>) {
            SimpleTest.runTest(X509NameTest())
        }
    }
}
