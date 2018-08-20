/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import ru.pearx.kormatter.conversions.Conversion
import ru.pearx.kormatter.conversions.UppercaseConversion
import ru.pearx.kormatter.utils.ConversionKey
import ru.pearx.kormatter.utils.MutableConversionMap
import ru.pearx.kormatter.utils.MutableFlagSet

/*
 * Created by mrAppleXZ on 12.08.18.
 */

@DslMarker
annotation class FormatterDsl

@FormatterDsl
class FormatterBuilder internal constructor()
{
    private val conversions: MutableConversionMap = hashMapOf()
    private val flags: MutableFlagSet = hashSetOf(FLAG_REUSE_ARGUMENT, FLAG_LEFT_JUSTIFIED)

    fun takeFrom(formatter: Formatter)
    {
        flags.takeFrom(formatter)
        conversions.takeFrom(formatter)
    }

    fun conversions(init: ConversionsScope.() -> Unit)
    {
        val scope = ConversionsScope(conversions)
        scope.init()
    }

    fun flags(init: FlagsScope.() -> Unit)
    {
        val scope = FlagsScope(flags)
        scope.init()
    }

    fun createFormatter() = Formatter(conversions, flags)
}

@FormatterDsl
class ConversionsScope internal constructor(val conversions: MutableConversionMap)
{
    fun takeFrom(formatter: Formatter)
    {
        conversions.takeFrom(formatter)
    }

    operator fun Char.invoke(conversion: Conversion, uppercaseVariant: Boolean = true)
    {
        put(ConversionKey(this), conversion, uppercaseVariant)
    }

    operator fun String.invoke(conversion: Conversion, uppercaseVariant: Boolean = true)
    {
        val key = when (this.length)
        {
            1 -> ConversionKey(this[0])
            2 -> ConversionKey(this[0], this[1])
            else -> throw IllegalArgumentException("$this: The conversion key string should be 1 or 2 characters long.")
        }
        put(key, conversion, uppercaseVariant)
    }

    private fun put(key: ConversionKey, conversion: Conversion, uppercaseVariant: Boolean)
    {
        val prev = conversions[key]
        if(prev != null)
            throw ConversionAlreadyExistsException(key, prev)
        conversions[key] = conversion
        if(uppercaseVariant)
            put(key.withConversion(key.conversion.toUpperCase()), UppercaseConversion(conversion), false)
    }
}

@FormatterDsl
class FlagsScope internal constructor(val flags: MutableFlagSet)
{
    fun takeFrom(formatter: Formatter)
    {
        flags.takeFrom(formatter)
    }

    operator fun Char.unaryPlus()
    {
        flags.add(this)
    }
}

fun buildFormatter(init: FormatterBuilder.() -> Unit): Formatter
{
    val bld = FormatterBuilder()
    bld.init()
    return bld.createFormatter()
}

private fun MutableConversionMap.takeFrom(formatter: Formatter) = putAll(formatter.conversions)
private fun MutableFlagSet.takeFrom(formatter: Formatter) = addAll(formatter.flags)