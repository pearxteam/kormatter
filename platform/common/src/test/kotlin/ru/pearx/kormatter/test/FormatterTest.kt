/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.test

import com.soywiz.klock.DateTime
import com.soywiz.klock.Klock
import ru.pearx.kormatter.*
import ru.pearx.kormatter.exceptions.IllegalConversionException
import kotlin.math.E
import kotlin.test.*


/*
 * Created by mrAppleXZ on 01.08.18.
 */
class FormatterTest
{
    @Test
    fun testSimple()
    {
        assertEquals("The Igor's middle name is gt22.", "The %s's middle name is %s.".format("Igor", "gt22"))
        assertEquals("The 128 is 128, the 256 is 0x100.", "The 128 is %d, the 256 is 0x%h.".format(128, 256))
    }

    //todo Remove this later
    @Test
    fun testFromDocs()
    {
        assertEquals(" d  c  b  a", "%4\$2s %3\$2s %2\$2s %1\$2s".format("a", "b", "c", "d"))
        assertEquals("e =    +2,7183", "e = %+10.4f".format(E))
        assertEquals("Amount gained or lost since last statement: \$ (6,217.58)", "Amount gained or lost since last statement: \$ %(,.2f".format(6217.581231214))
        assertEquals("Local time: 13:34:18", "Local time: %tT".format(DateTime.fromUnix(1533216858)))
        assertEquals("Unable to open file 'food': No such file or directory", "Unable to open file '%1\$s': %2\$s".format("food", Exception("No such file or directory")))
        assertEquals("Duke's Birthday: May 23, 1995", "Duke's Birthday: %1\$tb %1\$te, %1\$tY".format(DateTime.createClamped(1995, 5, 23)))
    }

    //todo Debug only
    @Test
    fun testPerformance()
    {
        val arr = arrayOf(
                "%4\$2s %3\$2s %2\$2s %1\$2s",
                "e = %+10.4f",
                "Amount gained or lost since last statement: \$ %(,.2f",
                "Local time: %tT",
                "Unable to open file '%1\$s': %2\$s",
                "Duke's Birthday: %1\$tb %1\$te, %1\$tY"
        )
        val t = Klock.currentTimeMillis()
        for (i in 1..1000)
            for (el in arr)
            {
                val tm = Klock.currentTimeMillis()
                Formatter.DEFAULT.format(el)
                println(Klock.currentTimeMillis() - tm)
            }
        println("Total: ${Klock.currentTimeMillis() - t}")
    }

    @Test
    fun testWithoutFormatting()
    {
        assertEquals("Test", "Test".format())
        assertEquals("Look at me, I'm a really long sentence. Trust me.", "Look at me, I'm a really long sentence. Trust me.".format("one", 1, '1'))
    }

    @Test
    fun testPercent()
    {
        assertEquals("Percent: %.", "Percent: %%.".format())
    }

    @Test
    fun testNewline()
    {
        val formatted = "Some%nText%nYay".format()
        assertTrue { arrayOf("Some\nText\nYay", "Some\rText\rYay", "Some\r\nText\r\nYay").contains(formatted) }
    }

    @Test
    fun testWidth()
    {
        assertEquals("         %", "%10%".format())
        assertEquals("%         ", "%-10%".format())
        assertEquals("      true", "%10b".format(true))
    }

    @Test
    fun testBoolean()
    {
        assertEquals("true", "%b".format(true))
        assertEquals("false", "%b".format(false))
        assertEquals("false", "%b".format(null))
        assertEquals("true", "%b".format("not a boolean"))
    }

    @Test
    fun testUppercase()
    {
        assertEquals("TRUE", "%B".format(true))
        assertEquals("FALSE", "%B".format(false))
    }

    @Test
    fun testString()
    {
        assertEquals("Stringy", "%s".format("Stringy"))
        assertEquals("1024", "%s".format(1024))
        assertEquals("null", "%s".format(null))
    }

    @Test
    fun testHashcode()
    {
        assertEquals("0x400", "0x%h".format(1024))
        assertEquals("13bb", "%h".format(5051))
        assertEquals("723b6f3b", "%h".format("NAN ACHTUNG"))
    }

    @Test
    fun testIllegalConversions()
    {
        assertFailsWith<IllegalConversionException> { "Hello, %ы".format() }
        assertFailsWith<IllegalConversionException> { "Tsss, %2$12p".format() }
        assertFailsWith<IllegalConversionException> { "Tsss, %2$<12.420q".format() }
    }
}