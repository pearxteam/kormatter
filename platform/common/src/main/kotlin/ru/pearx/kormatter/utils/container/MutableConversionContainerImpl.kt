/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.container

import ru.pearx.kormatter.conversions.Conversion
import ru.pearx.kormatter.exceptions.ConversionAlreadyExistsException


/*
 * Created by mrAppleXZ on 12.08.18.
 */
internal class MutableConversionContainerImpl private constructor(private val conversions: MutableMap<Char?, MutableMap<Char, Conversion>>) : MutableConversionContainer, Map<Char?, Map<Char, Conversion>> by conversions
{
    constructor() : this(HashMap<Char?, MutableMap<Char, Conversion>>())

    override fun add(prefix: Char?, char: Char, toPut: Conversion, uppercaseVariant: Boolean)
    {
        var conversionsForPrefix = conversions[prefix]
        if (conversionsForPrefix == null)
        {
            //add new prefix
            val lst = HashMap<Char, Conversion>()
            conversions[prefix] = lst
            conversionsForPrefix = lst
        }

        putConversion(conversionsForPrefix, prefix, char, toPut)
        if (uppercaseVariant)
            putConversion(conversionsForPrefix, prefix, char.toUpperCase(), ru.pearx.kormatter.conversions.UppercaseConversion(toPut))
    }

    private fun putConversion(conversionsForPrefix: MutableMap<Char, Conversion>, prefix: Char?, char: Char, toPut: Conversion)
    {
        val existing = conversionsForPrefix[char]
        if (existing != null)
            throw ConversionAlreadyExistsException(prefix, char, existing)

        conversionsForPrefix[char] = toPut
    }

    override fun remove(prefix: Char?, char: Char): Boolean
    {
        val conversionsForPrefix = conversions[prefix] ?: return false

        if (conversionsForPrefix.remove(char) != null)
        {
            return true
        }
        return false
    }
}