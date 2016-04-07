/*
 * $Id: d49f5ce17f409a60813b5d81489c1efb73f0dfbe $
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

 * @author  Paulo Soares
 */
class CMYKColor
/**
 * Construct a CMYK Color.
 * @param floatCyan
 * *
 * @param floatMagenta
 * *
 * @param floatYellow
 * *
 * @param floatBlack
 */
(floatCyan: Float, floatMagenta: Float, floatYellow: Float, floatBlack: Float) : ExtendedColor(ExtendedColor.TYPE_CMYK, 1f - floatCyan - floatBlack, 1f - floatMagenta - floatBlack, 1f - floatYellow - floatBlack) {
    /**
     * @return the cyan value
     */
    var cyan: Float = 0.toFloat()
        internal set
    /**
     * @return the magenta value
     */
    var magenta: Float = 0.toFloat()
        internal set
    /**
     * @return the yellow value
     */
    var yellow: Float = 0.toFloat()
        internal set
    /**
     * @return the black value
     */
    var black: Float = 0.toFloat()
        internal set

    /**
     * Constructs a CMYK Color based on 4 color values (values are integers from 0 to 255).
     * @param intCyan
     * *
     * @param intMagenta
     * *
     * @param intYellow
     * *
     * @param intBlack
     */
    constructor(intCyan: Int, intMagenta: Int, intYellow: Int, intBlack: Int) : this(intCyan / 255f, intMagenta / 255f, intYellow / 255f, intBlack / 255f) {
    }

    init {
        cyan = ExtendedColor.normalize(floatCyan)
        magenta = ExtendedColor.normalize(floatMagenta)
        yellow = ExtendedColor.normalize(floatYellow)
        black = ExtendedColor.normalize(floatBlack)
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is CMYKColor)
            return false
        return cyan == obj.cyan && magenta == obj.magenta && yellow == obj.yellow && black == obj.black
    }

    override fun hashCode(): Int {
        return java.lang.Float.floatToIntBits(cyan) xor java.lang.Float.floatToIntBits(magenta) xor java.lang.Float.floatToIntBits(yellow) xor java.lang.Float.floatToIntBits(black)
    }

    companion object {

        private val serialVersionUID = 5940378778276468452L
    }

}
