/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.formatter

import ru.pearx.kormatter.conversions.conversion
import ru.pearx.kormatter.conversions.conversionNotNull
import ru.pearx.kormatter.flags.*
import ru.pearx.kormatter.formatter.builder.buildFormatter
import ru.pearx.kormatter.utils.PartAction
import ru.pearx.kormatter.utils.internal.lineSeparator


/*
 * Created by mrAppleXZ on 13.08.18.
 */
val DefaultFormatter = buildFormatter {
    conversions {
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

        's'(conversion
        { _, arg ->
            arg.toString()
        })

        'h'(conversionNotNull
        { _, arg ->
            arg.hashCode().toString(16)
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