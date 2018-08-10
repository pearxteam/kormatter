package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class BaseConversionNotNull(
        private val formatter: (FormatString, Any) -> String,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL
) : Conversion()
{
    override val canTakeArguments: Boolean
        get() = true

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, vararg args: Any?): String
    {
        val arg = takeArgument(str, indexHolder, *args)
        return when(arg)
        {
            null -> "null"
            else -> formatter(str, arg)
        }
    }
}