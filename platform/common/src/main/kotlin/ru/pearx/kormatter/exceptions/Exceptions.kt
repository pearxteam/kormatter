/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.conversions.Conversion
import ru.pearx.kormatter.utils.ConversionKey
import ru.pearx.kormatter.utils.FormatString


/*
 * Created by mrAppleXZ on 02.08.18.
 */
abstract class FormatStringException : RuntimeException
{
    protected abstract val localMessage: String
    val formatString: FormatString

    constructor(formatString: FormatString) : super()
    {
        this.formatString = formatString
    }

    constructor(formatString: FormatString, cause: Throwable?) : super(cause)
    {
        this.formatString = formatString
    }

    override val message: String?
        get() = "$formatString: $localMessage"
}

open class IllegalPartException(formatString: FormatString, private val name: String) : FormatStringException(formatString)
{
    override val localMessage: String
        get()
        {
            return "The format string shouldn't have the $name."
        }
}

class IllegalPrecisionException(formatString: FormatString) : IllegalPartException(formatString, "precision")

class IllegalWidthException(formatString: FormatString) : IllegalPartException(formatString, "width")

class IllegalConversionException(formatString: FormatString) : FormatStringException(formatString)
{
    override val localMessage: String
        get() = "Cannot find a conversion '${formatString.conversion}'."
}

class ConversionAlreadyExistsException(val key: ConversionKey, val existing: Conversion) : RuntimeException()
{
    override val message: String?
        get() = "The conversion '$key' already exists: $existing!"
}

open class IllegalFormatArgumentException(
        formatString: FormatString,
        val argument: Any?,
        override val localMessage: String = "'$argument' is not a valid argument for this conversion."
) : FormatStringException(formatString)

class IllegalFormatCodePointException(
        formatString: FormatString,
        val codePoint: Int
) : IllegalFormatArgumentException(formatString, "Illegal code point: $codePoint!")

open class IllegalFlagsException(
        formatString: FormatString,
        override val localMessage: String
) : FormatStringException(formatString)

open class NoSuchArgumentException(
        formatString: FormatString,
        override val localMessage: String,
        cause: Throwable?
) : FormatStringException(formatString, cause)