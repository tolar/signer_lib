/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  This code was originally part of the Apache Harmony project.
 *  The Apache Harmony project has been discontinued.
 *  That's why we imported the code into iText.
 */


/*
 * THE FILE HAS BEEN AUTOGENERATED BY MSGTOOL TOOL.
 * All changes made to this file manually will be overwritten 
 * if this tool runs again. Better make changes in the template file.
 */

package com.itextpdf.awt.geom.misc

import java.util.MissingResourceException
import java.util.ResourceBundle

/**
 * This class retrieves strings from a resource bundle and returns them,
 * formatting them with MessageFormat when required.
 *
 *
 * It is used by the system classes to provide national language support, by
 * looking up messages in the `
 * com.itextpdf.text.geom.misc.messages
` *
 * resource bundle. Note that if this file is not available, or an invalid key
 * is looked up, or resource bundle support is not available, the key itself
 * will be returned as the associated message. This means that the *KEY*
 * should a reasonable human-readable (English) string.

 */
object Messages {

    // ResourceBundle holding the system messages.
    private val bundle: ResourceBundle? = null

    /**
     * Retrieves a message which has no arguments.

     * @param msg
     * *            String the key to look up.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String): String {
        if (bundle == null)
            return msg
        try {
            return bundle.getString(msg)
        } catch (e: MissingResourceException) {
            return "Missing message: " + msg //$NON-NLS-1$
        }

    }

    /**
     * Retrieves a message which takes 1 argument.

     * @param msg
     * *            String the key to look up.
     * *
     * @param arg
     * *            Object the object to insert in the formatted output.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String, arg: Any): String {
        return getString(msg, arrayOf(arg))
    }

    /**
     * Retrieves a message which takes 1 integer argument.

     * @param msg
     * *            String the key to look up.
     * *
     * @param arg
     * *            int the integer to insert in the formatted output.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String, arg: Int): String {
        return getString(msg, arrayOf<Any>(Integer.toString(arg)))
    }

    /**
     * Retrieves a message which takes 1 character argument.

     * @param msg
     * *            String the key to look up.
     * *
     * @param arg
     * *            char the character to insert in the formatted output.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String, arg: Char): String {
        return getString(msg, arrayOf<Any>(arg.toString()))
    }

    /**
     * Retrieves a message which takes 2 arguments.

     * @param msg
     * *            String the key to look up.
     * *
     * @param arg1
     * *            Object an object to insert in the formatted output.
     * *
     * @param arg2
     * *            Object another object to insert in the formatted output.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String, arg1: Any, arg2: Any): String {
        return getString(msg, arrayOf(arg1, arg2))
    }

    /**
     * Retrieves a message which takes several arguments.

     * @param msg
     * *            String the key to look up.
     * *
     * @param args
     * *            Object[] the objects to insert in the formatted output.
     * *
     * @return String the message for that key in the system message bundle.
     */
    fun getString(msg: String, args: Array<Any>): String {
        var format = msg

        if (bundle != null) {
            try {
                format = bundle.getString(msg)
            } catch (e: MissingResourceException) {
            }

        }

        return format(format, args)
    }

    /**
     * Generates a formatted text string given a source string containing
     * "argument markers" of the form "{argNum}" where each argNum must be in
     * the range 0..9. The result is generated by inserting the toString of each
     * argument into the position indicated in the string.
     *
     *
     * To insert the "{" character into the output, use a single backslash
     * character to escape it (i.e. "\{"). The "}" character does not need to be
     * escaped.

     * @param format
     * *            String the format to use when printing.
     * *
     * @param args
     * *            Object[] the arguments to use.
     * *
     * @return String the formatted message.
     */
    fun format(format: String, args: Array<Any>): String {
        val answer = StringBuilder(format.length + args.size * 20)
        val argStrings = arrayOfNulls<String>(args.size)
        for (i in args.indices) {
            if (args[i] == null)
                argStrings[i] = "<null>"    //$NON-NLS-1$
            else
                argStrings[i] = args[i].toString()
        }
        var lastI = 0
        var i = format.indexOf('{', 0)
        while (i >= 0) {
            if (i != 0 && format[i - 1] == '\\') {
                // It's escaped, just print and loop.
                if (i != 1)
                    answer.append(format.substring(lastI, i - 1))
                answer.append('{')
                lastI = i + 1
            } else {
                // It's a format character.
                if (i > format.length - 3) {
                    // Bad format, just print and loop.
                    answer.append(format.substring(lastI, format.length))
                    lastI = format.length
                } else {
                    val argnum = Character.digit(format[i + 1],
                            10).toByte().toInt()
                    if (argnum < 0 || format[i + 2] != '}') {
                        // Bad format, just print and loop.
                        answer.append(format.substring(lastI, i + 1))
                        lastI = i + 1
                    } else {
                        // Got a good one!
                        answer.append(format.substring(lastI, i))
                        if (argnum >= argStrings.size)
                            answer.append("<missing argument>")    //$NON-NLS-1$
                        else
                            answer.append(argStrings[argnum])
                        lastI = i + 3
                    }
                }
            }
            i = format.indexOf('{',
                    lastI)
        }
        if (lastI < format.length)
            answer.append(format.substring(lastI, format.length))
        return answer.toString()
    }
}
