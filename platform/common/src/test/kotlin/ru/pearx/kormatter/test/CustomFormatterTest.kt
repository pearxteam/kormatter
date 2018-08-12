/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.test

import ru.pearx.kormatter.conversions.conversion
import ru.pearx.kormatter.formatter.builder.buildFormatter
import ru.pearx.kormatter.formatter.format
import kotlin.test.Test
import kotlin.test.assertEquals


/*
 * Created by mrAppleXZ on 12.08.18.
 */
class CustomFormatterTest
{
    @Test
    fun testCustomFormatter()
    {
        val form = buildFormatter {
            conversions {
                'g'(conversion
                { str, arg, to ->
                    var n = 0
                    for (ch in arg.toString())
                    {
                        to.append(if(n % 2 == 0) ch.toUpperCase() else ch.toLowerCase())
                        n++
                    }
                })
            }
        }
        assertEquals("Test", form.format("%s", "Test"))
        assertEquals("FuZzY StRiNg YaY", form.format("%g %g %g", "FUzzY", "STRING", "yay"))
    }
}