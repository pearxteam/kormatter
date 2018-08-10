/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.parser

import ru.pearx.kormatter.utils.singleEmptyNull
import ru.pearx.kormatter.utils.toIntEmptyNull


/*
 * Created by mrAppleXZ on 05.08.18.
 */
object FormatStringParser
{
    fun parse(format: String, regex: Regex): Iterator<FormatString>
    {
        return object : Iterator<FormatString>
        {
            private var next = regex.find(format)

            override fun hasNext(): Boolean = next != null

            override fun next(): FormatString
            {
                if(hasNext())
                {
                    val nxt: MatchResult = next!!
                    val result = FormatString(
                            nxt.groupValues[1].toIntEmptyNull(),
                            nxt.groupValues[2],
                            nxt.groupValues[3].toIntEmptyNull(),
                            nxt.groupValues[4].toIntEmptyNull(),
                            nxt.groupValues[5].singleEmptyNull(),
                            nxt.groupValues[6].single(),
                            nxt.range.start,
                            nxt.range.endInclusive
                    )
                    next = nxt.next()
                    return result
                }
                throw NoSuchElementException()
            }
        }
    }
}