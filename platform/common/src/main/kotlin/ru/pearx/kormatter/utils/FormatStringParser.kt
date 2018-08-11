/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils

import kotlin.coroutines.experimental.buildIterator


/*
 * Created by mrAppleXZ on 05.08.18.
 */
internal fun parseFormatString(format: String, regex: Regex): Iterator<FormatString>
{
    return object : Iterator<FormatString>
    {
        private var next: MatchResult? = regex.find(format)

        override fun hasNext(): Boolean = next != null

        override fun next(): FormatString
        {
            val nxt = next ?: throw NoSuchElementException()

            val result = FormatString(
                    argumentIndex = nxt["argumentIndex"].intNonEmpty(),
                    flags = nxt["flags"],
                    width = nxt["width"].intNonEmpty(),
                    precision = nxt["precision"].intNonEmpty(),
                    prefix = nxt["prefix"].singleNonEmpty(),
                    conversion = nxt["conversion"].single(),
                    start = nxt.range.start,
                    endInclusive = nxt.range.endInclusive
            )
            next = nxt.next()
            return result
        }
    }
}