/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.container

import ru.pearx.kormatter.conversions.Conversion


/*
 * Created by mrAppleXZ on 13.08.18.
 */
interface MutableConversionContainer : ConversionContainer
{
    fun add(prefix: Char?, char: Char, toPut: Conversion, uppercaseVariant: Boolean = true)

    fun add(char: Char, toPut: Conversion, uppercaseVariant: Boolean = true) = add(null, char, toPut, uppercaseVariant)

    fun remove(prefix: Char?, char: Char): Boolean

    fun remove(conversion: Char): Boolean = remove(null, conversion)
}