/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversions

import ru.pearx.kormatter.utils.ArgumentTaker
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.PartAction


/*
 * Created by mrAppleXZ on 09.08.18.
 */
class UppercaseConversion(private val baseConversion: Conversion) : Conversion
{
    override val widthAction: PartAction
        get() = baseConversion.widthAction

    override val precisionAction: PartAction
        get() = baseConversion.precisionAction

    override val canTakeArguments: Boolean
        get() = baseConversion.canTakeArguments

    override fun format(str: FormatString, taker: ArgumentTaker, to: Appendable) = baseConversion.format(str, taker, ru.pearx.kormatter.conversions.UppercaseConversion.UppercaseAppendable(to))

    override fun check(str: FormatString) = baseConversion.check(str)

    private class UppercaseAppendable(private val to: Appendable) : Appendable
    {
        override fun append(c: Char): Appendable
        {
            return to.apply {
                append(c.toUpperCase())
            }
        }

        override fun append(csq: CharSequence?): Appendable
        {
            return to.apply {
                if (csq == null)
                    append("NULL")
                else
                {
                    for (ch in csq)
                    {
                        append(ch.toUpperCase())
                    }
                }
            }
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): Appendable
        {
            return to.apply {
                val s = csq ?: "NULL"

                if (start < 0 || start > end || end > s.length)
                    throw IndexOutOfBoundsException("start $start, end $end, s.length() ${s.length}")

                for (i in start..(end - 1))
                {
                    append(s[i].toUpperCase())
                }
            }
        }
    }
}