package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 08.08.18.
 */
open class BaseConversion(
        private val formatter: (FormatString, Any?) -> String,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL
) : Conversion()
{
    override val canTakeArguments: Boolean
        get() = true

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, vararg args: Any?): String = formatter(str, takeArgument(str, indexHolder, *args))
}