/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import ru.pearx.kormatter.conversion.*
import ru.pearx.kormatter.conversion.elements.*
import ru.pearx.kormatter.exceptions.ConversionAlreadyExistsException
import ru.pearx.kormatter.exceptions.IllegalConversionException
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.parser.FormatStringParser
import ru.pearx.kormatter.utils.ArgumentIndexHolder
import ru.pearx.kormatter.utils.lineSeparator

/*
 * Created by mrAppleXZ on 04.07.18 18:11
 */

open class Formatter(addStandardConversions: Boolean)
{
    private lateinit var regex: Regex
    private val _shouldRecompileRegex: AtomicInt = atomic(1)

    protected var shouldRecompileRegex: Boolean
        get() = _shouldRecompileRegex.value == 1
        set(value)
        {
            _shouldRecompileRegex.value = if(value) 1 else 0
        }
    protected val conversions: ConversionContainer = ConversionContainer()
    protected val flags: MutableList<Char> = ArrayList(listOf(FLAG_LEFT_JUSTIFIED, FLAG_REUSE_ARGUMENT_INDEX, FLAG_ALTERNATE_FORM, FLAG_INCLUDE_SIGN, FLAG_POSITIVE_LEADING_SPACE, FLAG_ZERO_PADDED, FLAG_LOCALE_SPECIFIC_GROUPING_SEPARATORS, FLAG_NEGATIVE_PARENTHESES))

    init
    {
        if(addStandardConversions)
            conversions.addStandardConversions()
    }

    fun <T : Appendable> format(format: String, to: T, vararg args: Any?): T
    {
        recompileRegex()
        val indexHolder = ArgumentIndexHolder(-1, -1)

        var textStart = 0
        for (str in FormatStringParser.parse(format, regex))
        {
            to.append(format.substring(textStart, str.start))
            textStart = str.endInclusive + 1

            val conversion = conversions.get(str.prefix, str.conversion)
                    ?: throw IllegalConversionException(str)
            conversion.check(str)

            val formatted = conversion.format(str, indexHolder, *args)
            //todo precision
            appendWithWidth(str, formatted, to)
        }
        to.append(format.substring(textStart))

        return to
    }

    private fun appendWithWidth(str: FormatString, formatted: String, to: Appendable)
    {
        if (str.width != null)
        {
            val len = str.width - formatted.length
            if (len > 0)
            {
                val reverse = str.flags.contains(FLAG_LEFT_JUSTIFIED)
                if (reverse)
                    to.append(formatted)
                for (n in 1..len)
                    to.append(' ')
                if (!reverse)
                    to.append(formatted)
                return
            }
        }
        to.append(formatted)
    }

    private fun recompileRegex()
    {
        if (_shouldRecompileRegex.compareAndSet(1, 0))
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

    fun format(format: String, vararg args: Any?): String = format(format, StringBuilder(), *args).toString()

    protected inner class ConversionContainer : Map<Char?, MutableMap<Char, IConversion>>
    {
        private val conversions: MutableMap<Char?, MutableMap<Char, IConversion>> = HashMap()

        fun addStandardConversions()
        {
            add('%', SimpleConversion("%", precisionDependency = PartDependency.FORBIDDEN))
            add('n', SimpleConversion(lineSeparator, PartDependency.FORBIDDEN, PartDependency.FORBIDDEN))
            add('b', BaseConversion(
                    { _, arg ->
                        when (arg)
                        {
                            null -> "false"
                            is Boolean -> arg.toString()
                            else -> "true"
                        }
                    }
            ), true)
            add('s', BaseConversion(
                    { _, arg -> arg.toString() } //todo Formattable support
            ), true)
            add('h', BaseConversionNotNull(
                    { _, arg -> arg.hashCode().toString(16) }
            ), true)
        }

        fun add(prefix: Char?, char: Char, toPut: IConversion, uppercaseVariant: Boolean = false)
        {
            var conversionsForPrefix = conversions[prefix]
            if (conversionsForPrefix == null)
            {
                //add new prefix
                val lst = HashMap<Char, IConversion>()
                conversions[prefix] = lst
                conversionsForPrefix = lst
                if (prefix != null)
                    shouldRecompileRegex = true
            }


            putConversion(conversionsForPrefix, prefix, char, toPut)
            if (uppercaseVariant)
                putConversion(conversionsForPrefix, prefix, char.toUpperCase(), UppercaseConversion(toPut))
        }

        private fun putConversion(conversionsForPrefix: MutableMap<Char, IConversion>, prefix: Char?, char: Char, toPut: IConversion)
        {
            val existing = conversionsForPrefix[char]
            if (existing != null)
                throw ConversionAlreadyExistsException(prefix, char, existing)

            conversionsForPrefix[char] = toPut
        }

        fun add(char: Char, toPut: IConversion, uppercaseVariant: Boolean = false) = add(null, char, toPut, uppercaseVariant)

        fun remove(prefix: Char?, char: Char): Boolean
        {
            val conversionsForPrefix = conversions[prefix] ?: return false

            if (conversionsForPrefix.remove(char) != null)
            {
                if (conversionsForPrefix.isEmpty())
                    shouldRecompileRegex = true
                return true
            }
            return false
        }

        fun remove(conversion: Char): Boolean = remove(null, conversion)

        fun get(prefix: Char?, char: Char): IConversion?
        {
            val conversionsForPrefix = conversions[prefix] ?: return null
            return conversionsForPrefix[char]
        }

        fun get(conversion: Char): IConversion? = get(null, conversion)

        override val entries: Set<Map.Entry<Char?, MutableMap<Char, IConversion>>>
            get() = conversions.entries

        override val keys: Set<Char?>
            get() = conversions.keys

        override val size: Int
            get() = conversions.size

        override val values: Collection<MutableMap<Char, IConversion>>
            get() = conversions.values

        override fun containsKey(key: Char?): Boolean = conversions.containsKey(key)

        override fun containsValue(value: MutableMap<Char, IConversion>): Boolean = conversions.containsValue(value)

        override fun get(key: Char?): MutableMap<Char, IConversion>? = conversions.get(key)

        override fun isEmpty(): Boolean = conversions.isEmpty()
    }

    companion object
    {
        val DEFAULT = Formatter(true)
    }
}