/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.formatter

import ru.pearx.kormatter.exceptions.IllegalConversionException
import ru.pearx.kormatter.flags.FLAG_LEFT_JUSTIFIED
import ru.pearx.kormatter.flags.FLAG_REUSE_ARGUMENT_INDEX
import ru.pearx.kormatter.utils.ArgumentTaker
import ru.pearx.kormatter.utils.FlagContainer
import ru.pearx.kormatter.utils.MutableFlagContainer
import ru.pearx.kormatter.utils.container.ConversionContainer
import ru.pearx.kormatter.utils.container.MutableConversionContainer
import ru.pearx.kormatter.utils.container.MutableConversionContainerImpl
import ru.pearx.kormatter.utils.internal.ArgumentIndexHolder
import ru.pearx.kormatter.utils.internal.createFormatStringRegex
import ru.pearx.kormatter.utils.internal.parseFormatString


/*
 * Created by mrAppleXZ on 12.08.18.
 */
class Formatter internal constructor()
{
    private val regex: Regex by lazy { createFormatStringRegex(flags, conversions) }
    internal val conversionsMutable: MutableConversionContainer = MutableConversionContainerImpl()
    internal val flagsMutable: MutableFlagContainer = arrayListOf(FLAG_REUSE_ARGUMENT_INDEX, FLAG_LEFT_JUSTIFIED)

    val conversions: ConversionContainer = conversionsMutable
    val flags: FlagContainer = flagsMutable

    fun <T : Appendable> format(format: String, to: T, args: Array<out Any?>): T
    {
        return to.apply {
            val taker = ArgumentTaker(ArgumentIndexHolder(-1, -1), args)
            var textStart = 0

            for (str in parseFormatString(format, regex))
            {
                append(format, textStart, str.start)
                textStart = str.endInclusive + 1

                taker.formatString = str
                val conversion = conversions[str.prefix, str.conversion] ?: throw IllegalConversionException(str)
                conversion.check(str)

                if (str.width != null)
                {
                    val formatted = StringBuilder().apply { conversion.format(str, taker, this) }

                    val len = str.width - formatted.length
                    if (len > 0)
                    {
                        val leftJustify = str.flags.contains(FLAG_LEFT_JUSTIFIED)
                        if (leftJustify)
                            append(formatted)
                        for (n in 1..len)
                            append(' ')
                        if (!leftJustify)
                            append(formatted)
                    }
                    else
                        append(formatted)
                }
                else
                    conversion.format(str, taker, this)
            }
            append(format, textStart, format.length)
        }
    }
}