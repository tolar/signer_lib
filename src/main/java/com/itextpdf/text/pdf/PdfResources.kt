/*
 * $Id: 22fbad8106b671e2dc3a51108c54b5f668f63fd8 $
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

/**
 * PdfResources is the PDF Resources-object.
 *
 * The marking operations for drawing a page are stored in a stream that is the value of the
 * Contents key in the Page object's dictionary. Each marking context includes a list
 * of the named resources it uses. This resource list is stored as a dictionary that is the
 * value of the context's Resources key, and it serves two functions: it enumerates
 * the named resources in the contents stream, and it established the mapping from the names
 * to the objects used by the marking operations.
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.5 (page 195-197).

 * @see PdfPage
 */

internal class PdfResources : PdfDictionary() {

    // methods

    fun add(key: PdfName, resource: PdfDictionary) {
        if (resource.size() == 0)
            return
        val dic = getAsDict(key)
        if (dic == null)
            put(key, resource)
        else
            dic.putAll(resource)
    }
}// constructor
/**
 * Constructs a PDF ResourcesDictionary.
 */
