/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import ru.pearx.kormatter.conversion.IConversion
import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.conversion.SimpleConversion
import ru.pearx.kormatter.parser.FormatStringParser

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
    private val _shouldRecompileRegex: AtomicBoolean = atomic(true)

    protected var shouldRecompileRegex: Boolean
        get() = _shouldRecompileRegex.value
        set(value)
        {
            _shouldRecompileRegex.value = value
        }
    protected val conversions: ConversionContainer = ConversionContainer()
    protected val flags: MutableList<Char> = ArrayList(listOf(FLAG_LEFT_JUSTIFIED, FLAG_REUSE_ARGUMENT_INDEX, FLAG_ALTERNATE_FORM, FLAG_INCLUDE_SIGN, FLAG_POSITIVE_LEADING_SPACE, FLAG_ZERO_PADDED, FLAG_LOCALE_SPECIFIC_GROUPING_SEPARATORS, FLAG_NEGATIVE_PARENTHESES))

    init
    {
        conversions.add(SimpleConversion('%', PartDependency.OPTIONAL, PartDependency.FORBIDDEN, "%"))
        conversions.add(SimpleConversion('n', PartDependency.FORBIDDEN, PartDependency.FORBIDDEN, lineSeparator))
    }

    fun <T : Appendable> format(format: String, to: T, vararg args: Any?): T
    {
        recompileRegex()

        var textStart = 0
        for (str in FormatStringParser.parse(format, regex))
        {
            textStart = str.endInclusive

            val conversion = conversions.get(str.prefix, str.conversion)
                    ?: throw IllegalConversionException("Cannot find a conversion '${str.conversion}' with prefix '${str.prefix}'.")
            conversion.check(str)

        }
        to.append(format.substring(textStart))

        return to
    }

    private fun recompileRegex()
    {
        if (_shouldRecompileRegex.compareAndSet(true, false))
        {
            //%[argumentIndex$][flags][width][.precision][prefix]conversion
            //%(?:(\d+)\$)?([ALLOWED_FLAGS]+)?(\d+)?(?:\.(\d+))?([ALLOWED_PREFIXES])?(.)
            val sb = StringBuilder()
            sb.append("""%(?:(\d+)\$)?([""")
            sb.append(Regex.escape(String(flags.toCharArray())))
            sb.append("""]+)?(\d+)?(?:\.(\d+))?(""")

            val sbPrefixes = StringBuilder(conversions.size)
            for (ch in conversions.keys)
                if (ch != null)
                    sbPrefixes.append(ch)
            if (!sbPrefixes.isEmpty())
            {
                sb.append("[").append(Regex.escape(sbPrefixes.toString())).append("]")
            }

            sb.append(""")?(.)""")

            this.regex = Regex(sb.toString())
        }
    }

    fun format(format: String, vararg args: Any?): String = format(format, StringBuilder(), args).toString()

    inner class ConversionContainer : Map<Char?, MutableList<IConversion>>
    {
        private val conversions: MutableMap<Char?, MutableList<IConversion>> = HashMap()

        fun add(prefix: Char?, toPut: IConversion): Boolean
        {
            var convs = conversions[prefix]
            if (convs == null)
            {
                val lst = ArrayList<IConversion>()
                conversions[prefix] = lst
                convs = lst
                shouldRecompileRegex = true
            }
            for (conv in convs)
                if (conv.character == toPut.character)
                    throw ConversionAlreadyExistsException("The conversion of character '${conv.character}' already exists.")
            return convs.add(toPut)
        }

        fun add(toPut: IConversion): Boolean = add(null, toPut)

        fun remove(prefix: Char?, conversion: Char): Boolean
        {
            val convs = conversions[prefix] ?: return false

            val it = convs.listIterator()
            while (it.hasNext())
            {
                if (it.next().character == conversion)
                {
                    it.remove()
                    if (convs.isEmpty())
                    {
                        shouldRecompileRegex = true
                        conversions.remove(prefix)
                    }
                    return true
                }
            }
            return false
        }

        fun remove(conversion: Char): Boolean = remove(null, conversion)

        fun get(prefix: Char?, conversion: Char): IConversion?
        {
            val lst = conversions[prefix] ?: return null
            for (conv in lst)
            {
                if (conv.character == conversion)
                    return conv
            }
            return null
        }

        fun get(conversion: Char): IConversion? = get(null, conversion)

        override val entries: Set<Map.Entry<Char?, MutableList<IConversion>>>
            get() = conversions.entries

        override val keys: Set<Char?>
            get() = conversions.keys

        override val size: Int
            get() = conversions.size

        override val values: Collection<MutableList<IConversion>>
            get() = conversions.values

        override fun containsKey(key: Char?): Boolean = conversions.containsKey(key)

        override fun containsValue(value: MutableList<IConversion>): Boolean = conversions.containsValue(value)

        override fun get(key: Char?): MutableList<IConversion>? = conversions[key]

        override fun isEmpty(): Boolean = conversions.isEmpty()
    }
}