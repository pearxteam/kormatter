/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversions

import ru.pearx.kormatter.utils.ArgumentTaker
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.PartDependency

/*
 * Created by mrAppleXZ on 12.08.18.
 */
fun conversion(replacement: String, widthDependency: PartDependency = PartDependency.OPTIONAL, precisionDependency: PartDependency = PartDependency.OPTIONAL) : Conversion
{
    return ConversionConstant(replacement, widthDependency, precisionDependency)
}

fun conversion(supportedFlags: CharArray = charArrayOf(), widthDependency: PartDependency = PartDependency.OPTIONAL, precisionDependency: PartDependency = PartDependency.OPTIONAL, executor: (str: FormatString, arg: Any?, to: Appendable) -> Unit) : Conversion
{
    return object : ConversionExecuting(supportedFlags, widthDependency, precisionDependency)
    {
        override fun format(str: FormatString, taker: ArgumentTaker, to: Appendable)
        {
            executor(str, taker.take(), to)
        }
    }
}

fun conversion(supportedFlags: CharArray = charArrayOf(), widthDependency: PartDependency = PartDependency.OPTIONAL, precisionDependency: PartDependency = PartDependency.OPTIONAL, executor: (str: FormatString, arg: Any?) -> String) : Conversion
{
    return object : ConversionExecuting(supportedFlags, widthDependency, precisionDependency)
    {
        override fun format(str: FormatString, taker: ArgumentTaker, to: Appendable)
        {
            to.append(executor(str, taker.take()))
        }
    }
}

fun conversionNotNull(supportedFlags: CharArray = charArrayOf(), widthDependency: PartDependency = PartDependency.OPTIONAL, precisionDependency: PartDependency = PartDependency.OPTIONAL, executor: (str: FormatString, arg: Any, to: Appendable) -> Unit) : Conversion
{
    return object : ConversionExecutingNotNull(supportedFlags, widthDependency, precisionDependency)
    {
        override fun format(str: FormatString, arg: Any, to: Appendable)
        {
            executor(str, arg, to)
        }
    }
}

fun conversionNotNull(supportedFlags: CharArray = charArrayOf(), widthDependency: PartDependency = PartDependency.OPTIONAL, precisionDependency: PartDependency = PartDependency.OPTIONAL, executor: (str: FormatString, arg: Any) -> String) : Conversion
{
    return object : ConversionExecutingNotNull(supportedFlags, widthDependency, precisionDependency)
    {
        override fun format(str: FormatString, arg: Any, to: Appendable)
        {
            to.append(executor(str, arg))
        }
    }
}