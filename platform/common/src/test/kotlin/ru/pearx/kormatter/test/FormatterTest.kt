package ru.pearx.kormatter.test

import com.soywiz.klock.Klock
import ru.pearx.kormatter.format
import kotlin.test.Test
import kotlin.test.assertEquals


/*
 * Created by mrAppleXZ on 01.08.18.
 */
class FormatterTest
{
    @Test
    fun testSimple()
    {
        assertEquals("The Igor's middle name is gt22.", "The %s's surname is %s.".format("Igor", "gt22"))
        assertEquals("The 128 is 128, the 256 is 0x100.", "The 128 is %d, the 256 is 0x%h.".format(128, 256))
    }
}