/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.test

import ru.pearx.kormatter.conversions.conversion
import ru.pearx.kormatter.exceptions.IllegalFlagsException
import ru.pearx.kormatter.formatter.DefaultFormatter
import ru.pearx.kormatter.formatter.builder.buildFormatter
import ru.pearx.kormatter.formatter.format
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


/*
 * Created by mrAppleXZ on 12.08.18.
 */
class CustomFormatterTest
{
    @Test
    fun testCustomConversions()
    {
        val form = buildFormatter {
            takeFrom(DefaultFormatter)
            conversions {
                'g'(conversion
                { to, _, arg ->
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

    @Test
    fun testCustomFlags()
    {
        val flagCaseSwitch = '!'
        val form = buildFormatter {
            takeFrom(DefaultFormatter)
            conversions {
                'q'(conversion(supportedFlags = charArrayOf(flagCaseSwitch))
                { str, arg ->
                    arg.toString().run { if(flagCaseSwitch in str.flags) toUpperCase() else toLowerCase() }
                })
            }
            flags {
                +flagCaseSwitch
            }
        }
        assertEquals("Test", form.format("%s", "Test"))
        assertFailsWith<IllegalFlagsException> { form.format("%!s", "Test") }
        assertEquals("TEATIME", form.format("%!q", "teaTIME"))
        assertEquals("teatime", form.format("%q", "TEAtime"))
        assertFailsWith<IllegalFlagsException> { form.format("%#q", "Test") }
    }
}