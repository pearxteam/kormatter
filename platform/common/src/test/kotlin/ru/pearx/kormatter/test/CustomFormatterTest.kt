package ru.pearx.kormatter.test

import ru.pearx.kormatter.Formatter
import ru.pearx.kormatter.conversion.elements.AppendingConversionNotNull
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
                conversions.add('r', AppendingConversionNotNull(
                { str, to, arg ->
                    val string = arg.toString()
                    for(i in string.length-1 downTo 0)
                    {
                        to.append(string[i])
                    }
                }), true)
            }
        }
        assertEquals("desrever", form.format("%r", "reversed"))
        assertEquals("DESREVER", form.format("%R", "reversed"))
        assertEquals("  desrever", form.format("%10r", "reversed"))
    }
}