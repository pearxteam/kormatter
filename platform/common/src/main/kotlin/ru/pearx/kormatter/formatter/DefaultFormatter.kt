/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.formatter

import ru.pearx.kormatter.conversions.conversion
import ru.pearx.kormatter.conversions.conversionNotNull
import ru.pearx.kormatter.exceptions.IllegalFormatCodePointException
import ru.pearx.kormatter.flags.*
import ru.pearx.kormatter.formatter.builder.buildFormatter
import ru.pearx.kormatter.utils.PartAction
import ru.pearx.kormatter.utils.internal.lineSeparator


/*
 * Created by mrAppleXZ on 13.08.18.
 */
val DefaultFormatter = buildFormatter {
    conversions {
        /* Some notes for me:
         * Deny precision for character, integral, date/time, percent, line separator, 'g' conversions
         * Deny width for line separator conversion.
         */
        '%'(conversion("%", precisionAction = PartAction.FORBIDDEN), false)

        'n'(conversion(lineSeparator, PartAction.FORBIDDEN, PartAction.FORBIDDEN), false)

        'b'(conversion
        { _, arg ->
            when (arg)
            {
                null -> "false"
                is Boolean -> arg.toString()
                else -> "true"
            }
        })

        's'(conversion(supportedFlags = charArrayOf('#'))
        { to, str, arg ->
            if (arg is Formattable)
                arg.formatTo(to, str)
            else
                to.append(arg.toString())
        })

        'h'(conversionNotNull
        { _, arg ->
            arg.hashCode().toString(16)
        })

        'c'(conversionNotNull(precisionAction = PartAction.FORBIDDEN)
        { to, str, arg ->
            when (arg)
            {
                is Char -> to.append(arg)
                is Int, is Short, is Byte ->
                {
                    val i = (arg as Number).toInt()

                    if (i ushr 16 < (0X10FFFF + 1).ushr(16)) // isValidCodePoint; 0X10FFFF - MAX_CODE_POINT
                    {
                        if (i ushr 16 == 0) //isBmpCodePoint
                            to.append(i.toChar())
                        else
                        {
                            to.append(((i ushr 10) + (Char.MIN_HIGH_SURROGATE.toInt() - (0x010000 ushr 10))).toChar()) // high surrogate; 0x010000 - MIN_SUPPLEMENTARY_CODE_POINT
                            to.append(((i and 0x3ff) + Char.MIN_LOW_SURROGATE.toInt()).toChar()) // low surrogate
                        }
                    }
                    else
                        throw IllegalFormatCodePointException(str, i)
                }
            }
        })
    }
    flags {
        +FLAG_ALTERNATE_FORM
        +FLAG_INCLUDE_SIGN
        +FLAG_POSITIVE_LEADING_SPACE
        +FLAG_ZERO_PADDED
        +FLAG_LOCALE_SPECIFIC_GROUPING_SEPARATORS
        +FLAG_NEGATIVE_PARENTHESES
    }
}