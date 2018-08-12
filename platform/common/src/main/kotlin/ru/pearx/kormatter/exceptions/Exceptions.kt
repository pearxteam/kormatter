/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.conversions.Conversion
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.PartDependency


/*
 * Created by mrAppleXZ on 02.08.18.
 */
class IllegalPrecisionException(formatString: FormatString, dependency: PartDependency) : PartDependencyException(formatString, dependency, "precision")

class IllegalWidthException(formatString: FormatString, dependency: PartDependency) : PartDependencyException(formatString, dependency, "width")

class IllegalConversionException(formatString: FormatString) : FormatStringException(formatString)
{
    override val localMessage: String
        get() = "Cannot find a conversion '${formatString.prefix ?: ""}${formatString.conversion}'."
}

class ConversionAlreadyExistsException(val prefix: Char?, val conversion: Char, val existing: Conversion) : RuntimeException()
{
    override val message: String?
        get() = "The conversion of character '${prefix ?: ""}$conversion' already exists: $existing!"
}

class IllegalFormatCodePointException(
        formatString: FormatString,
        val codePoint: Int
) : IllegalFormatArgumentException(formatString, "Illegal code point: $codePoint!")