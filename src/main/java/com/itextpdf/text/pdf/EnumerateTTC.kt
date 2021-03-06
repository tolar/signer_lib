/*
 * $Id: 40c4f6d31bb8051d79637112d445761b3f5a26ed $
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
import java.util.HashMap

import com.itextpdf.text.DocumentException
import com.itextpdf.text.error_messages.MessageLocalization

/** Enumerates all the fonts inside a True Type Collection.

 * @author  Paulo Soares
 */
internal class EnumerateTTC : TrueTypeFont {

    var names: Array<

            String>
        protected set

    @Throws(DocumentException::class, IOException::class)
    constructor(ttcFile: String) {
        fileName = ttcFile
        rf = RandomAccessFileOrArray(ttcFile)
        findNames()
    }

    @Throws(DocumentException::class, IOException::class)
    constructor(ttcArray: ByteArray) {
        fileName = "Byte array TTC"
        rf = RandomAccessFileOrArray(ttcArray)
        findNames()
    }

    @Throws(DocumentException::class, IOException::class)
    fun findNames() {
        tables = HashMap<String, IntArray>()

        try {
            val mainTag = readStandardString(4)
            if (mainTag != "ttcf")
                throw DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttc.file", fileName))
            rf!!.skipBytes(4)
            val dirCount = rf!!.readInt()
            names = arrayOfNulls<String>(dirCount)
            val dirPos = rf!!.filePointer.toInt()
            for (dirIdx in 0..dirCount - 1) {
                tables.clear()
                rf!!.seek(dirPos.toLong())
                rf!!.skipBytes(dirIdx * 4)
                directoryOffset = rf!!.readInt()
                rf!!.seek(directoryOffset.toLong())
                if (rf!!.readInt() != 0x00010000)
                    throw DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttf.file", fileName))
                val num_tables = rf!!.readUnsignedShort()
                rf!!.skipBytes(6)
                for (k in 0..num_tables - 1) {
                    val tag = readStandardString(4)
                    rf!!.skipBytes(4)
                    val table_location = IntArray(2)
                    table_location[0] = rf!!.readInt()
                    table_location[1] = rf!!.readInt()
                    tables.put(tag, table_location)
                }
                names[dirIdx] = baseFont
            }
        } finally {
            if (rf != null)
                rf!!.close()
        }
    }

}
