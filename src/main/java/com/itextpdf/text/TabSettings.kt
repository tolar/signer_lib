/*
 * $Id: 71be3d82c6a3e15c30ed417e1f53b6772a37dce5 $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2016 iText Group NV
 * Authors: Bruno Lowagie, Eugene Markovskyi, et al.
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

import java.util.*

class TabSettings {

    var tabStops: List<TabStop>? = ArrayList<TabStop>()
    var tabInterval = DEFAULT_TAB_INTERVAL

    constructor() {
    }

    constructor(tabStops: List<TabStop>) {
        this.tabStops = tabStops
    }

    constructor(tabInterval: Float) {
        this.tabInterval = tabInterval
    }

    constructor(tabStops: List<TabStop>, tabInterval: Float) {
        this.tabStops = tabStops
        this.tabInterval = tabInterval
    }

    fun getTabStopNewInstance(currentPosition: Float): TabStop {
        var tabStop: TabStop? = null
        if (tabStops != null) {
            for (currentTabStop in tabStops!!) {
                if (currentTabStop.position - currentPosition > 0.001) {
                    tabStop = TabStop(currentTabStop)
                    break
                }
            }
        }

        if (tabStop == null) {
            tabStop = TabStop.newInstance(currentPosition, tabInterval)
        }

        return tabStop
    }

    companion object {
        val DEFAULT_TAB_INTERVAL = 36f

        fun getTabStopNewInstance(currentPosition: Float, tabSettings: TabSettings?): TabStop {
            if (tabSettings != null)
                return tabSettings.getTabStopNewInstance(currentPosition)
            return TabStop.newInstance(currentPosition, DEFAULT_TAB_INTERVAL)
        }
    }
}
