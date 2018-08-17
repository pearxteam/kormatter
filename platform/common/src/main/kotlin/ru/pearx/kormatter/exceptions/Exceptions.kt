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
    private val localMessage: String
    private val formatString: FormatString

    constructor(formatString: FormatString, localMessage: String) : super()
    {
        this.formatString = formatString
        this.localMessage = localMessage
    }

    constructor(formatString: FormatString, localMessage: String, cause: Throwable?) : super(cause)
    {
        this.formatString = formatString
        this.localMessage = localMessage
    }

    override val message: String?
        get() = "$formatString: $localMessage"
}

open class PartMismatchException(formatString: FormatString, name: String) : FormatStringException(formatString, "The format string shouldn't have the $name.")

class PrecisionMismatchException(formatString: FormatString) : PartMismatchException(formatString, "precision")

class WidthMismatchException(formatString: FormatString) : PartMismatchException(formatString, "width")

class UnknownConversionException(formatString: FormatString) : FormatStringException(formatString, "Cannot find a conversion '${formatString.conversion}'.")

class ConversionAlreadyExistsException(key: ConversionKey, existing: Conversion) : RuntimeException("The conversion '$key' already exists: $existing!")

open class IllegalFormatArgumentException : FormatStringException
{
    constructor(formatString: FormatString, argument: Any?) : super(formatString, "'$argument' of type '${argument?.let {it::class} ?: "NULL"}' is not a valid argument for this conversion.")
    constructor(formatString: FormatString, message: String) : super(formatString, message)
}

class IllegalFormatCodePointException(
        formatString: FormatString,
        codePoint: Int
) : IllegalFormatArgumentException(formatString, "Illegal UTF-16 code point: ${codePoint.hashCode().toString(16)}!")

class FlagMismatchException(
        formatString: FormatString,
        flag: Char
) : FormatStringException(formatString, "The '$flag' flag isn't an allowed flag for this conversion.")

open class NoSuchArgumentException(
        formatString: FormatString,
        message: String,
        cause: Throwable?
) : FormatStringException(formatString, message, cause)