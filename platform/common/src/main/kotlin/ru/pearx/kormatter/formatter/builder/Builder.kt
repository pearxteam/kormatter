/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.formatter.builder

import ru.pearx.kormatter.conversions.Conversion
import ru.pearx.kormatter.formatter.Formatter
import ru.pearx.kormatter.utils.MutableFlagContainer
import ru.pearx.kormatter.utils.container.MutableConversionContainer

/*
 * Created by mrAppleXZ on 12.08.18.
 */

@DslMarker
annotation class FormatterDsl

@FormatterDsl
class FormatterBuilder internal constructor()
{
    private val formatter = Formatter()

    fun conversions(init: ConversionsScope.() -> Unit)
    {
        val scope = ConversionsScope(formatter.conversionsMutable)
        scope.init()
    }

    fun flags(init: FlagsScope.() -> Unit)
    {
        val scope = FlagsScope(formatter.flagsMutable)
        scope.init()
    }

    fun build(): Formatter = formatter
}

@FormatterDsl
class ConversionsScope internal constructor(val conversions: MutableConversionContainer)
{
    operator fun Char.invoke(toPut: Conversion, uppercaseVariant: Boolean = true)
    {
        conversions.add(this, toPut, uppercaseVariant)
    }

    operator fun String.invoke(toPut: Conversion, uppercaseVariant: Boolean = true)
    {
        conversions.add(this[0], this[1], toPut, uppercaseVariant)
    }
}

@FormatterDsl
class FlagsScope internal constructor(val flags: MutableFlagContainer)
{
    operator fun Char.unaryPlus()
    {
        flags.add(this)
    }
}

fun buildFormatter(withDefaults: Boolean = true, init: FormatterBuilder.() -> Unit): Formatter
{
    val bld = FormatterBuilder()
    if(withDefaults)
        bld.addDefaults()
    bld.init()
    return bld.build()
}