/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils


/*
 * Created by mrAppleXZ on 13.08.18.
 */
data class ConversionKey(val prefix: Char?, val conversion: Char)
{
    constructor(conversion: Char) : this(null, conversion)

    fun withConversion(conversion: Char) = ConversionKey(this.prefix, conversion)

    override fun toString(): String
    {
        return "${prefix?.toString().orEmpty()}$conversion"
    }
}