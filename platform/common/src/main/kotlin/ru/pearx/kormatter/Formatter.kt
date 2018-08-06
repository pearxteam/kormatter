/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import kotlinx.atomicfu.atomic
import ru.pearx.kormatter.conversion.IConversion

/*
 * Created by mrAppleXZ on 04.07.18 18:11
 */

open class Formatter
{
    companion object
    {
        val DEFAULT = Formatter()
    }

    private lateinit var regex: Regex
    private val _shouldRecompileRegex = atomic(true)

    protected var shouldRecompileRegex
        get() = _shouldRecompileRegex.value
        set(value) { _shouldRecompileRegex.value = value }
    protected open val conversions: MutableMap<Char?, MutableList<IConversion>> = HashMap()
    protected open val flags: MutableList<Char> = ArrayList(listOf('-', '#', '+', ' ', '0', ',', '(', '<'))


    fun <T : Appendable> format(format: String, to: T, vararg args: Any?): T
    {
        recompileRegex()



        return to
    }

    private fun recompileRegex()
    {
        if (_shouldRecompileRegex.compareAndSet(true, false))
        {
            //%[argumentIndex$][flags][width][.precision][prepend]conversion
            //%(?:(\d+)\$)?([ALLOWED_FLAGS]+)?(\d+)?(?:\.(\d+))?([ALLOWED_PREPENDS])?(.)
            val sb = StringBuilder()
            sb.append("""%(?:(\d+)\$)?([""")
            sb.append(Regex.escape(String(flags.toCharArray())))
            sb.append("""]+)?(\d+)?(?:\.(\d+))?(""")

            val sbPrepends = StringBuilder(conversions.size)
            for (ch in conversions.keys)
                if (ch != null)
                    sbPrepends.append(ch)
            if(!sbPrepends.isEmpty())
            {
                sb.append("[").append(Regex.escape(sbPrepends.toString())).append("]")
            }

            sb.append(""")?(.)""")

            this.regex = Regex(sb.toString())
        }
    }

    fun format(format: String, vararg args: Any?): String = format(format, StringBuilder(), args).toString()
}