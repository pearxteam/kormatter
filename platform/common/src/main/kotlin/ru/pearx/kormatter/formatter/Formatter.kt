/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.formatter

import ru.pearx.kormatter.exceptions.UnknownConversionException
import ru.pearx.kormatter.flags.FLAG_LEFT_JUSTIFIED
import ru.pearx.kormatter.utils.ArgumentTaker
import ru.pearx.kormatter.utils.ConversionMap
import ru.pearx.kormatter.utils.FlagSet
import ru.pearx.kormatter.utils.PartAction
import ru.pearx.kormatter.utils.internal.ArgumentIndexHolder
import ru.pearx.kormatter.utils.internal.createFormatStringRegex
import ru.pearx.kormatter.utils.internal.lengthSequence
import ru.pearx.kormatter.utils.internal.parseFormatString


/*
 * Created by mrAppleXZ on 12.08.18.
 */
class Formatter internal constructor(val conversions: ConversionMap, val flags: FlagSet)
{
    private val regex: Regex by lazy { createFormatStringRegex(flags, conversions) }

    fun <T : Appendable> formatTo(to: T, format: String, args: Array<out Any?>): T
    {
        return to.apply {
            val taker = ArgumentTaker(ArgumentIndexHolder(-1, -1), args)
            var textStart = 0

            for (str in parseFormatString(format, regex))
            {
                append(format, textStart, str.start)
                textStart = str.endInclusive + 1

                taker.formatString = str
                val conversion = conversions[str.conversion] ?: throw UnknownConversionException(str)
                conversion.check(str)

                val fWidth = conversion.widthAction == PartAction.STANDARD && str.width != null
                val fPrecision = conversion.precisionAction == PartAction.STANDARD && str.precision != null
                if (fWidth || fPrecision) // Apply standard width or precision post-processing
                {
                    var formatted: CharSequence = StringBuilder().apply { conversion.formatTo(this, str, taker) }

                    if(fPrecision) // Cut $formatted using precision
                    {
                        if(formatted.length - str.precision!! > 0)
                        {
                            formatted = formatted.lengthSequence(str.precision) //todo: Replace it with formatted.setLength(cut) when this method will come to the stdlib (ha-ha, I'm so naive).
                        }
                    }

                    if(fWidth) // Print $formatted with specified width
                    {
                        val len = str.width!! - formatted.length
                        if (len > 0)
                        {
                            val leftJustify = FLAG_LEFT_JUSTIFIED in str.flags
                            if (leftJustify)
                                append(formatted)
                            for (n in 1..len)
                                append(' ')
                            if (!leftJustify)
                                append(formatted)
                            continue
                        }
                    }

                    // Print $formatted
                    append(formatted)
                }
                else // Format the conversion directly
                    conversion.formatTo(this, str, taker)
            }
            append(format, textStart, format.length)
        }
    }
}