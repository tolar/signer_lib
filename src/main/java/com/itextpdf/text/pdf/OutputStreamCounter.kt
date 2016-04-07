/*
 * $Id: 37a83744982ca7d842eda604ab3016b05e360e00 $
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

 * @author  psoares
 */
class OutputStreamCounter
/** Creates a new instance of OutputStreamCounter  */
(protected var out: OutputStream) : OutputStream() {
    var counter: Long = 0
        protected set

    /** Closes this output stream and releases any system resources
     * associated with this stream. The general contract of `close`
     * is that it closes the output stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     *
     *
     * The `close` method of `OutputStream` does nothing.

     * @exception  IOException  if an I/O error occurs.
     */
    @Throws(IOException::class)
    override fun close() {
        out.close()
    }

    /** Flushes this output stream and forces any buffered output bytes
     * to be written out. The general contract of `flush` is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     *
     *
     * The `flush` method of `OutputStream` does nothing.

     * @exception  IOException  if an I/O error occurs.
     */
    @Throws(IOException::class)
    override fun flush() {
        out.flush()
    }

    /** Writes `b.length` bytes from the specified byte array
     * to this output stream. The general contract for `write(b)`
     * is that it should have exactly the same effect as the call
     * `write(b, 0, b.length)`.

     * @param      b   the data.
     * *
     * @exception  IOException  if an I/O error occurs.
     * *
     * @see java.io.OutputStream.write
     */
    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        counter += b.size.toLong()
        out.write(b)
    }

    /** Writes the specified byte to this output stream. The general
     * contract for `write` is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument `b`. The 24
     * high-order bits of `b` are ignored.
     *
     *
     * Subclasses of `OutputStream` must provide an
     * implementation for this method.

     * @param      b   the `byte`.
     * *
     * @exception  IOException  if an I/O error occurs. In particular,
     * *             an `IOException` may be thrown if the
     * *             output stream has been closed.
     */
    @Throws(IOException::class)
    override fun write(b: Int) {
        ++counter
        out.write(b)
    }

    /** Writes `len` bytes from the specified byte array
     * starting at offset `off` to this output stream.
     * The general contract for `write(b, off, len)` is that
     * some of the bytes in the array `b` are written to the
     * output stream in order; element `b[off]` is the first
     * byte written and `b[off+len-1]` is the last byte written
     * by this operation.
     *
     *
     * The `write` method of `OutputStream` calls
     * the write method of one argument on each of the bytes to be
     * written out. Subclasses are encouraged to override this method and
     * provide a more efficient implementation.
     *
     *
     * If `b` is `null`, a
     * `NullPointerException` is thrown.
     *
     *
     * If `off` is negative, or `len` is negative, or
     * `off+len` is greater than the length of the array
     * `b`, then an IndexOutOfBoundsException is thrown.

     * @param      b     the data.
     * *
     * @param      off   the start offset in the data.
     * *
     * @param      len   the number of bytes to write.
     * *
     * @exception  IOException  if an I/O error occurs. In particular,
     * *             an `IOException` is thrown if the output
     * *             stream is closed.
     */
    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        counter += len.toLong()
        out.write(b, off, len)
    }

    fun resetCounter() {
        counter = 0
    }
}
