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

internal class ConversionConstant(
        private val replacement: String,
        override val widthDependency: PartDependency,
        override val precisionDependency: PartDependency
) : ConversionChecking
{
    override val canTakeArguments: Boolean
        get() = false

    override fun format(str: FormatString, taker: ArgumentTaker, to: Appendable)
    {
        to.append(replacement)
    }
}

internal abstract class ConversionExecuting(
        private val supportedFlags: CharArray,
        override val widthDependency: PartDependency,
        override val precisionDependency: PartDependency
) : ConversionChecking
{
    override val canTakeArguments: Boolean
        get() = true

    override fun checkFlag(str: FormatString, flag: Char): Boolean = supportedFlags.contains(flag)
}

internal abstract class ConversionExecutingNotNull(
        private val supportedFlags: CharArray,
        override val widthDependency: PartDependency,
        override val precisionDependency: PartDependency
) : ConversionChecking
{
    override val canTakeArguments: Boolean
        get() = true

    override fun checkFlag(str: FormatString, flag: Char): Boolean = supportedFlags.contains(flag)

    override fun format(str: FormatString, taker: ArgumentTaker, to: Appendable)
    {
        val arg = taker.take()
        if(arg == null)
            to.append("null")
        else
            format(str, arg, to)
    }

    abstract fun format(str: FormatString, arg: Any, to: Appendable)
}