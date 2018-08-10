/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.test

import ru.pearx.kormatter.Formatter
import ru.pearx.kormatter.conversion.elements.base.ConversionNotNull
import kotlin.test.Test
import kotlin.test.assertEquals


/*
 * Created by mrAppleXZ on 10.08.18.
 */
class CustomFormatterTest
{
    @Test
    fun testCustomFormatter()
    {
        val form = object : Formatter(true)
        {
            init
            {
                conversions.add('r', ConversionNotNull
                { _, to, arg ->
                    val string = arg.toString()
                    for (i in string.length - 1 downTo 0)
                    {
                        to.append(string[i])
                    }
                }, true)
            }
        }
        assertEquals("desrever", form.format("%r", "reversed"))
        assertEquals("DESREVER", form.format("%R", "reversed"))
        assertEquals("  desrever", form.format("%10r", "reversed"))
    }
}