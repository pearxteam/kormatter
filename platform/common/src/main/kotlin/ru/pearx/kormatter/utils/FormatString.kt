/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils


/*
 * Created by mrAppleXZ on 05.08.18.
 */
data class FormatString(
        val argumentIndex: Int?,
        val flags: String,
        val width: Int?,
        val precision: Int?,
        val conversion: ConversionKey,
        val start: Int,
        val endInclusive: Int
) {
    override fun toString(): String
    {
        return StringBuilder().apply {
            append("%")
            if (argumentIndex != null)
                append(argumentIndex).append("$")
            if (width != null)
                append(width)
            if (precision != null)
                append(".").append(precision)
            append(conversion)
        }.toString()
    }
}