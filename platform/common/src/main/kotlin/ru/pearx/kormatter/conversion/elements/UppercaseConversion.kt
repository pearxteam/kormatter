package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 09.08.18.
 */
class UppercaseConversion(private val baseConversion: IConversion) : IConversion
{
    override val widthDependency: PartDependency
        get() = baseConversion.widthDependency

    override val precisionDependency: PartDependency
        get() = baseConversion.precisionDependency

    override val canTakeArguments: Boolean
        get() = baseConversion.canTakeArguments

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, vararg args: Any?): String = baseConversion.format(str, indexHolder, *args).toUpperCase()

    override fun check(str: FormatString) = baseConversion.check(str)
}