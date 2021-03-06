/*
 * $Id: 96a1c0a344e4ac617a535ff808d82373cc493123 $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 2014-2015 iText Group NV
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
 *
 *
 * This class is based on the C# open source freeware library Clipper:
 * http://www.angusj.com/delphi/clipper.php
 * The original classes were distributed under the Boost Software License:
 *
 * Freeware for both open source and commercial applications
 * Copyright 2010-2014 Angus Johnson
 * Boost Software License - Version 1.0 - August 17th, 2003
 *
 * Permission is hereby granted, free of charge, to any person or organization
 * obtaining a copy of the software and accompanying documentation covered by
 * this license (the "Software") to use, reproduce, display, distribute,
 * execute, and transmit the Software, and to prepare derivative works of the
 * Software, and to permit third-parties to whom the Software is furnished to
 * do so, all subject to the following:
 *
 * The copyright notices in the Software and this entire statement, including
 * the above license grant, this restriction and the following disclaimer,
 * must be included in all copies of the Software, in whole or in part, and
 * all derivative works of the Software, unless such copies or derivative
 * works are solely in the form of machine-executable object code generated by
 * a source language processor.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE
 * FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.itextpdf.text.pdf.parser.clipper

import java.math.BigInteger
import java.util.Comparator

abstract class Point<T : Number> protected constructor(protected var x: T, protected var y: T, protected var z: T) where T : Comparable<T> {
    class DoublePoint : Point<Double> {

        @JvmOverloads constructor(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) : super(x, y, z) {
        }

        constructor(other: DoublePoint) : super(other) {
        }

        val x: Double
            get() = x

        val y: Double
            get() = y

        val z: Double
            get() = z
    }

    class LongPoint : Point<Long> {

        constructor(x: Double, y: Double) : this(x.toLong(), y.toLong()) {
        }

        @JvmOverloads constructor(x: Long = 0, y: Long = 0, z: Long = 0) : super(x, y, z) {
        }

        constructor(other: LongPoint) : super(other) {
        }

        val x: Long
            get() = x

        val y: Long
            get() = y

        val z: Long
            get() = z

        companion object {
            fun getDeltaX(pt1: LongPoint, pt2: LongPoint): Double {
                if (pt1.y == pt2.y) {
                    return Edge.HORIZONTAL
                } else {
                    return (pt2.x - pt1.x).toDouble() / (pt2.y - pt1.y)
                }
            }
        }
    }

    private class NumberComparator<T : Number> : Comparator<T> where T : Comparable<T> {

        @Throws(ClassCastException::class)
        override fun compare(a: T, b: T): Int {
            return a.compareTo(b)
        }
    }

    protected constructor(pt: Point<T>) : this(pt.x, pt.y, pt.z) {
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj is Point<*>) {
            return NUMBER_COMPARATOR.compare(x, obj.x) == 0 && NUMBER_COMPARATOR.compare(y, obj.y) == 0
        } else {
            return false
        }
    }

    fun set(other: Point<T>) {
        x = other.x
        y = other.y
        z = other.z
    }

    fun setX(x: T) {
        this.x = x
    }

    fun setY(y: T) {
        this.y = y
    }

    fun setZ(z: T) {
        this.z = z
    }

    override fun toString(): String {
        return "Point [x=$x, y=$y, z=$z]"
    }

    companion object {

        internal fun arePointsClose(pt1: Point<out Number>, pt2: Point<out Number>, distSqrd: Double): Boolean {
            val dx = pt1.x.toDouble() - pt2.x.toDouble()
            val dy = pt1.y.toDouble() - pt2.y.toDouble()
            return dx * dx + dy * dy <= distSqrd
        }

        internal fun distanceFromLineSqrd(pt: Point<out Number>, ln1: Point<out Number>, ln2: Point<out Number>): Double {
            //The equation of a line in general form (Ax + By + C = 0)
            //given 2 points (x�,y�) & (x�,y�) is ...
            //(y� - y�)x + (x� - x�)y + (y� - y�)x� - (x� - x�)y� = 0
            //A = (y� - y�); B = (x� - x�); C = (y� - y�)x� - (x� - x�)y�
            //perpendicular distance of point (x�,y�) = (Ax� + By� + C)/Sqrt(A� + B�)
            //see http://en.wikipedia.org/wiki/Perpendicular_distance
            val A = ln1.y.toDouble() - ln2.y.toDouble()
            val B = ln2.x.toDouble() - ln1.x.toDouble()
            var C = A * ln1.x.toDouble() + B * ln1.y.toDouble()
            C = A * pt.x.toDouble() + B * pt.y.toDouble() - C
            return C * C / (A * A + B * B)
        }

        internal fun getUnitNormal(pt1: LongPoint, pt2: LongPoint): DoublePoint {
            var dx = (pt2.x - pt1.x).toDouble()
            var dy = (pt2.y - pt1.y).toDouble()
            if (dx == 0.0 && dy == 0.0) {
                return DoublePoint()
            }

            val f = 1 * 1.0 / Math.sqrt(dx * dx + dy * dy)
            dx *= f
            dy *= f

            return DoublePoint(dy, -dx)
        }

        protected fun isPt2BetweenPt1AndPt3(pt1: LongPoint, pt2: LongPoint, pt3: LongPoint): Boolean {
            if (pt1 == pt3 || pt1 == pt2 || pt3 == pt2) {
                return false
            } else if (pt1.x !== pt3.x) {
                return pt2.x > pt1.x == pt2.x < pt3.x
            } else {
                return pt2.y > pt1.y == pt2.y < pt3.y
            }
        }

        protected fun slopesEqual(pt1: LongPoint, pt2: LongPoint, pt3: LongPoint, useFullRange: Boolean): Boolean {
            if (useFullRange) {
                return BigInteger.valueOf(pt1.y - pt2.y).multiply(BigInteger.valueOf(pt2.x - pt3.x)) == BigInteger.valueOf(pt1.x - pt2.x).multiply(BigInteger.valueOf(pt2.y - pt3.y))
            } else {
                return (pt1.y - pt2.y) * (pt2.x - pt3.x) - (pt1.x - pt2.x) * (pt2.y - pt3.y) == 0
            }
        }

        protected fun slopesEqual(pt1: LongPoint, pt2: LongPoint, pt3: LongPoint, pt4: LongPoint, useFullRange: Boolean): Boolean {
            if (useFullRange) {
                return BigInteger.valueOf(pt1.y - pt2.y).multiply(BigInteger.valueOf(pt3.x - pt4.x)) == BigInteger.valueOf(pt1.x - pt2.x).multiply(BigInteger.valueOf(pt3.y - pt4.y))
            } else {
                return (pt1.y - pt2.y) * (pt3.x - pt4.x) - (pt1.x - pt2.x) * (pt3.y - pt4.y) == 0
            }
        }

        internal fun slopesNearCollinear(pt1: LongPoint, pt2: LongPoint, pt3: LongPoint, distSqrd: Double): Boolean {
            //this function is more accurate when the point that's GEOMETRICALLY
            //between the other 2 points is the one that's tested for distance.
            //nb: with 'spikes', either pt1 or pt3 is geometrically between the other pts
            if (Math.abs(pt1.x - pt2.x) > Math.abs(pt1.y - pt2.y)) {
                if (pt1.x > pt2.x == pt1.x < pt3.x) {
                    return distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd
                } else if (pt2.x > pt1.x == pt2.x < pt3.x) {
                    return distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd
                } else {
                    return distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd
                }
            } else {
                if (pt1.y > pt2.y == pt1.y < pt3.y) {
                    return distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd
                } else if (pt2.y > pt1.y == pt2.y < pt3.y) {
                    return distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd
                } else {
                    return distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd
                }
            }
        }

        private val NUMBER_COMPARATOR = NumberComparator()
    }

}// end struct IntPoint