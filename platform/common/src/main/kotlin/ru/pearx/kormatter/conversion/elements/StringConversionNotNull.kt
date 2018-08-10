package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.ArgumentIndexHolder
import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 10.08.18.
 */
open class StringConversionNotNull(
        private val formatter: (FormatString, Any) -> String,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL
) : Conversion()
{
    override val canTakeArguments: Boolean
        get() = true

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, to: Appendable, vararg args: Any?)
    {
        val arg = takeArgument(str, indexHolder, *args)
        to.append(when(arg)
        {
            null -> "null"
            else -> formatter(str, arg)
        })
    }
}