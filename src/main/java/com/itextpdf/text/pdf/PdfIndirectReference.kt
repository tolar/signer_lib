/*
 * $Id: fe9ac95258ecf8aac42c67d623cd9a1c3a77f194 $
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
package com.itextpdf.text.pdf

import java.io.IOException
import java.io.OutputStream

/**
 * PdfIndirectReference contains a reference to a PdfIndirectObject.
 *
 * Any object used as an element of an array or as a value in a dictionary may be specified
 * by either a direct object of an indirect reference. An indirect reference is a reference
 * to an indirect object, and consists of the indirect object's object number, generation number
 * and the R keyword.
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.11 (page 54).

 * @see PdfObject

 * @see PdfIndirectObject
 */

open class PdfIndirectReference : PdfObject {

    // membervariables

    /** the object number  */
    // methods

    /**
     * Returns the number of the object.

     * @return        a number.
     */

    var number: Int = 0
        protected set

    /** the generation number  */
    /**
     * Returns the generation of the object.

     * @return        a number.
     */

    var generation = 0
        protected set

    // constructors

    protected constructor() : super(0) {
    }

    /**
     * Constructs a PdfIndirectReference.

     * @param        type            the type of the PdfObject that is referenced to
     * *
     * @param        number            the object number.
     * *
     * @param        generation        the generation number.
     */

    internal constructor(type: Int, number: Int, generation: Int) : super(0, StringBuffer().append(number).append(" ").append(generation).append(" R").toString()) {
        this.number = number
        this.generation = generation
    }

    /**
     * Constructs a PdfIndirectReference.

     * @param        type            the type of the PdfObject that is referenced to
     * *
     * @param        number            the object number.
     */

    protected constructor(type: Int, number: Int) : this(type, number, 0) {
    }

    override fun toString(): String {
        return StringBuffer().append(number).append(" ").append(generation).append(" R").toString()
    }

    @Throws(IOException::class)
    override fun toPdf(writer: PdfWriter, os: OutputStream) {
        os.write(PdfEncodings.convertToBytes(toString(), null))
    }
}
