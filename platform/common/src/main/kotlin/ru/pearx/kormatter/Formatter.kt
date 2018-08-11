/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import ru.pearx.kormatter.conversion.*
import ru.pearx.kormatter.conversion.elements.*
import ru.pearx.kormatter.conversion.elements.base.*
import ru.pearx.kormatter.exceptions.ConversionAlreadyExistsException
import ru.pearx.kormatter.exceptions.FormatterAlreadyBuiltException
import ru.pearx.kormatter.exceptions.IllegalConversionException
import ru.pearx.kormatter.utils.ArgumentIndexHolder
import ru.pearx.kormatter.utils.lineSeparator
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.parseFormatString

/*
 * Created by mrAppleXZ on 04.07.18 18:11
 */
open class Formatter(addStandardConversions: Boolean)
{
    private val regex = lazy {
        //%[argumentIndex$][flags][width][.precision][prefix]conversion
        //%(?:(?<argumentIndex>\d+)\$)?(?<flags>[ALLOWED_FLAGS]+)?(?<width>\d+)?(?:\.(?<precision>\d+))?(?<prefix>[ALLOWED_PREFIXES])?(?<conversion>.)
        val sb = StringBuilder()
        sb.append("""%(?:(?<argumentIndex>\d+)\$)?(?<flags>[""")
        sb.append(Regex.escape(String(flags.toCharArray())))
        sb.append("""]+)?(?<width>\d+)?(?:\.(?<precision>\d+))?(?<prefix>""")

        val sbPrefixes = StringBuilder(conversions.size)
        for (ch in conversions.keys)
            if (ch != null)
                sbPrefixes.append(ch)
        if (!sbPrefixes.isEmpty())
        {
            sb.append("[").append(Regex.escape(sbPrefixes.toString())).append("]")
        }

        sb.append(""")?(?<conversion>.)""")

        Regex(sb.toString())
    }
    protected val conversions: ConversionContainer = ConversionContainer()
    protected val flags: MutableList<Char> = ArrayList(listOf(FLAG_LEFT_JUSTIFIED, FLAG_REUSE_ARGUMENT_INDEX, FLAG_ALTERNATE_FORM, FLAG_INCLUDE_SIGN, FLAG_POSITIVE_LEADING_SPACE, FLAG_ZERO_PADDED, FLAG_LOCALE_SPECIFIC_GROUPING_SEPARATORS, FLAG_NEGATIVE_PARENTHESES))

    init
    {
        if (addStandardConversions)
            conversions.addStandardConversions()
    }

    fun <T : Appendable> format(format: String, to: T, vararg args: Any?): T
    {
        val indexHolder = ArgumentIndexHolder(-1, -1)

        var textStart = 0
        for (str in parseFormatString(format, regex.value))
        {
            to.append(format.substring(textStart, str.start))
            textStart = str.endInclusive + 1

            val conversion = conversions.get(str.prefix, str.conversion)
                    ?: throw IllegalConversionException(str)
            conversion.check(str)

            append(str, conversion, indexHolder, to, *args)
        }
        to.append(format.substring(textStart))

        return to
    }

    private fun append(str: FormatString, conversion: IConversion, indexHolder: ArgumentIndexHolder, to: Appendable, vararg args: Any?)
    {
        //todo precision
        if (str.width != null)
        {
            val formatted: StringBuilder = conversion.format(str, indexHolder, StringBuilder(), *args)
            val len = str.width - formatted.length
            if (len > 0)
            {
                val leftJustify = str.flags.contains(FLAG_LEFT_JUSTIFIED)
                if (leftJustify)
                    to.append(formatted)
                for (n in 1..len)
                    to.append(' ')
                if (!leftJustify)
                    to.append(formatted)
            }
            else
                to.append(formatted)
        }
        else
            conversion.format(str, indexHolder, to, *args)
    }

    fun format(format: String, vararg args: Any?): String = format(format, StringBuilder(), *args).toString()

    inner class ConversionContainer : Map<Char?, MutableMap<Char, IConversion>>
    {
        private val conversions: MutableMap<Char?, MutableMap<Char, IConversion>> = HashMap()

        fun addStandardConversions()
        {
            add('%', ConversionConstant("%", precisionDependency = PartDependency.FORBIDDEN))

            add('n', ConversionConstant(lineSeparator, PartDependency.FORBIDDEN, PartDependency.FORBIDDEN))
            add('b', Conversion
            { _, arg ->
                when (arg)
                {
                    null -> "false"
                    is Boolean -> arg.toString()
                    else -> "true"
                }
            }, true)
            add('s', Conversion { _, arg -> arg.toString() }, true)
            add('h', ConversionNotNull { _, arg -> arg.hashCode().toString(16) }, true)
            add('c', ConversionNotNull
            { _, app, arg ->
                when (arg)
                {
                    is Char -> app.append(arg)
                    is Byte, is Short, is Int ->
                    {
                    }
                }
            }, true)
        }

        fun add(prefix: Char?, char: Char, toPut: IConversion, uppercaseVariant: Boolean = false)
        {
            if (regex.isInitialized())
                throw FormatterAlreadyBuiltException()
            var conversionsForPrefix = conversions[prefix]
            if (conversionsForPrefix == null)
            {
                //add new prefix
                val lst = HashMap<Char, IConversion>()
                conversions[prefix] = lst
                conversionsForPrefix = lst
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
            if (regex.isInitialized())
                throw FormatterAlreadyBuiltException()
            val conversionsForPrefix = conversions[prefix] ?: return false

            if (conversionsForPrefix.remove(char) != null)
            {
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

        override fun get(key: Char?): MutableMap<Char, IConversion>? = conversions[key]

        override fun isEmpty(): Boolean = conversions.isEmpty()
    }

    companion object : Formatter(true)
}