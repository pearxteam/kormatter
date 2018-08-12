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
interface ConversionContainer : Map<Char?, Map<Char, Conversion>>
{
    operator fun get(prefix: Char?, char: Char): Conversion?
    {
        val conversionsForPrefix = get(prefix) ?: return null
        return conversionsForPrefix[char]
    }

    operator fun get(conversion: Char): Conversion? = get(null, conversion)
}