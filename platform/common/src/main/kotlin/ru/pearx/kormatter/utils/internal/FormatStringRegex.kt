/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.internal

import ru.pearx.kormatter.utils.ConversionMap
import ru.pearx.kormatter.utils.FlagSet


/*
 * Created by mrAppleXZ on 12.08.18.
 */
internal fun createFormatStringRegex(flags: FlagSet, conversions: ConversionMap): Regex
{
    return Regex(StringBuilder().apply {
        append("""%(?:(\d+)\$)?([""")
        append(Regex.escape(String(flags.toCharArray())))
        append("""]+)?(\d+)?(?:\.(\d+))?(""")

        val sbPrefixes = StringBuilder(conversions.size)
        for (ch in conversions.keys)
            if(ch.prefix != null)
                sbPrefixes.append(ch.prefix)
        if (!sbPrefixes.isEmpty())
        {
            append("[")
            append(Regex.escape(sbPrefixes.toString()))
            append("]")
        }

        append(""")?(.)""")
    }.toString())
}