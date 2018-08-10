package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 09.08.18.
 */
class UppercaseConversion(private val baseConversion: IConversion) : IConversion
{
    class UppercaseAppendable(private val to: Appendable) : Appendable
    {
        override fun append(c: Char): Appendable
        {
            to.append(c.toUpperCase())
            return this
        }

        override fun append(csq: CharSequence?): Appendable
        {
            if (csq == null)
                to.append("NULL")
            else
            {
                for (ch in csq)
                {
                    to.append(ch.toUpperCase())
                }
            }
            return this
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): Appendable
        {
            val s = csq ?: "NULL"

            if (start < 0 || start > end || end > s.length)
                throw IndexOutOfBoundsException("start $start, end $end, s.length() ${s.length}")

            for (i in start..(end - 1))
            {
                to.append(s[i].toUpperCase())
            }
            return this
        }
    }

    override val widthDependency: PartDependency
        get() = baseConversion.widthDependency

    override val precisionDependency: PartDependency
        get() = baseConversion.precisionDependency

    override val canTakeArguments: Boolean
        get() = baseConversion.canTakeArguments

    override fun <T : Appendable> format(str: FormatString, indexHolder: ArgumentIndexHolder, to: T, vararg args: Any?): T
    {
        baseConversion.format(str, indexHolder, UppercaseAppendable(to), *args)
        return to
    }

    override fun check(str: FormatString) = baseConversion.check(str)
}