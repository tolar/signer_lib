/*
 * $Id: 45002eb495d713bf5bfef3fd6ae5e09ebb7d9386 $
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
package com.itextpdf.text.pdf.collection

import com.itextpdf.text.pdf.PdfBoolean
import com.itextpdf.text.pdf.PdfDate
import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfNumber
import com.itextpdf.text.pdf.PdfObject
import com.itextpdf.text.pdf.PdfString
import com.itextpdf.text.error_messages.MessageLocalization

/**
 * @author blowagie
 */
class PdfCollectionField
/**
 * Creates a PdfCollectionField.
 * @param name        the field name
 * *
 * @param type        the field type
 */
(name: String,
 /**
  * The type of the PDF collection field.
  * @since 2.1.2 (was called `type` previously)
  */
 protected var fieldType: Int) : PdfDictionary(PdfName.COLLECTIONFIELD) {

    init {
        put(PdfName.N, PdfString(name, PdfObject.TEXT_UNICODE))
        when (fieldType) {
            else -> put(PdfName.SUBTYPE, PdfName.S)
            DATE -> put(PdfName.SUBTYPE, PdfName.D)
            NUMBER -> put(PdfName.SUBTYPE, PdfName.N)
            FILENAME -> put(PdfName.SUBTYPE, PdfName.F)
            DESC -> put(PdfName.SUBTYPE, PdfName.DESC)
            MODDATE -> put(PdfName.SUBTYPE, PdfName.MODDATE)
            CREATIONDATE -> put(PdfName.SUBTYPE, PdfName.CREATIONDATE)
            SIZE -> put(PdfName.SUBTYPE, PdfName.SIZE)
        }
    }

    /**
     * The relative order of the field name. Fields are sorted in ascending order.
     * @param i    a number indicating the order of the field
     */
    fun setOrder(i: Int) {
        put(PdfName.O, PdfNumber(i))
    }

    /**
     * Sets the initial visibility of the field.
     * @param visible    the default is true (visible)
     */
    fun setVisible(visible: Boolean) {
        put(PdfName.V, PdfBoolean(visible))
    }

    /**
     * Indication if the field value should be editable in the viewer.
     * @param editable    the default is false (not editable)
     */
    fun setEditable(editable: Boolean) {
        put(PdfName.E, PdfBoolean(editable))
    }

    /**
     * Checks if the type of the field is suitable for a Collection Item.
     */
    val isCollectionItem: Boolean
        get() {
            when (fieldType) {
                TEXT, DATE, NUMBER -> return true
                else -> return false
            }
        }

    /**
     * Returns a PdfObject that can be used as the value of a Collection Item.
     * @param v    value	the value that has to be changed into a PdfObject (PdfString, PdfDate or PdfNumber)
     */
    fun getValue(v: String): PdfObject {
        when (fieldType) {
            TEXT -> return PdfString(v, PdfObject.TEXT_UNICODE)
            DATE -> return PdfDate(PdfDate.decode(v))
            NUMBER -> return PdfNumber(v)
        }
        throw IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.not.an.acceptable.value.for.the.field.2", v, get(PdfName.N).toString()))
    }

    companion object {
        /** A possible type of collection field.  */
        val TEXT = 0
        /** A possible type of collection field.  */
        val DATE = 1
        /** A possible type of collection field.  */
        val NUMBER = 2
        /** A possible type of collection field.  */
        val FILENAME = 3
        /** A possible type of collection field.  */
        val DESC = 4
        /** A possible type of collection field.  */
        val MODDATE = 5
        /** A possible type of collection field.  */
        val CREATIONDATE = 6
        /** A possible type of collection field.  */
        val SIZE = 7
    }
}
