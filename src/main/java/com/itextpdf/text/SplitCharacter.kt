/*
 * $Id: 02d4ef2009035f35e48979bf282b8876b0792fa8 $
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
package com.itextpdf.text

import com.itextpdf.text.pdf.PdfChunk

/** Interface for customizing the split character.

 * @author Paulo Soares
 */

interface SplitCharacter {

    /**
     * Returns true if the character can split a line. The splitting implementation
     * is free to look ahead or look behind characters to make a decision.
     *
     *
     * The default implementation is:
     *
     *
     *
     * public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
     * char c;
     * if (ck == null)
     * c = cc[current];
     * else
     * c = (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
     * if (c <= ' ' || c == '-') {
     * return true;
     * }
     * if (c < 0x2e80)
     * return false;
     * return ((c >= 0x2e80 && c < 0xd7a0)
     * || (c >= 0xf900 && c < 0xfb00)
     * || (c >= 0xfe30 && c < 0xfe50)
     * || (c >= 0xff61 && c < 0xffa0));
     * }
     *
     * @param start the lower limit of cc inclusive
     * *
     * @param current the pointer to the character in cc
     * *
     * @param end the upper limit of cc exclusive
     * *
     * @param cc an array of characters at least end sized
     * *
     * @param ck an array of PdfChunk. The main use is to be able to call
     * * [PdfChunk.getUnicodeEquivalent]. It may be null
     * * or shorter than end. If null no conversion takes place.
     * * If shorter than end the last element is used
     * *
     * @return true if the character(s) can split a line
     */
    fun isSplitCharacter(start: Int, current: Int, end: Int, cc: CharArray, ck: Array<PdfChunk>): Boolean
}
