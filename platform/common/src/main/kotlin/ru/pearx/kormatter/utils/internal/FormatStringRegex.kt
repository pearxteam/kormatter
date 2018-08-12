/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.internal

import ru.pearx.kormatter.conversions.Conversion


/*
 * Created by mrAppleXZ on 12.08.18.
 */
internal fun createFormatStringRegex(flags: List<Char>, conversions: Map<Char?, Map<Char, Conversion>>): Regex
{
    return Regex(StringBuilder().apply {
        append("""%(?:(?<argumentIndex>\d+)\$)?(?<flags>[""")
        append(Regex.escape(String(flags.toCharArray())))
        append("""]+)?(?<width>\d+)?(?:\.(?<precision>\d+))?(?<prefix>""")

        val sbPrefixes = StringBuilder(conversions.size)
        for (ch in conversions.keys)
            if (ch != null)
                sbPrefixes.append(ch)
        if (!sbPrefixes.isEmpty())
        {
            append("[").append(Regex.escape(sbPrefixes.toString())).append("]")
        }

        append(""")?(?<conversion>.)""")
    }.toString())
}