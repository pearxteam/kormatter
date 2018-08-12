/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversions

import ru.pearx.kormatter.FLAG_LEFT_JUSTIFIED
import ru.pearx.kormatter.FLAG_REUSE_ARGUMENT_INDEX
import ru.pearx.kormatter.exceptions.IllegalFlagsException
import ru.pearx.kormatter.exceptions.IllegalPrecisionException
import ru.pearx.kormatter.exceptions.IllegalWidthException
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.PartDependency


/*
 * Created by mrAppleXZ on 12.08.18.
 */
interface ConversionChecking : Conversion
{
    override fun check(str: FormatString)
    {
        if(!widthDependency.check(str.width))
            throw IllegalWidthException(str, widthDependency)
        if(!precisionDependency.check(str.precision))
            throw IllegalPrecisionException(str, precisionDependency)
        for (flag in str.flags)
        {
            if (flag == FLAG_LEFT_JUSTIFIED && widthDependency != PartDependency.FORBIDDEN)
                continue
            if (flag == FLAG_REUSE_ARGUMENT_INDEX && canTakeArguments)
                continue
            if(checkFlag(str, flag))
                continue
            throw IllegalFlagsException(str, "The conversion doesn't support the '$flag' flag.")
        }
    }

    fun checkFlag(str: FormatString, flag: Char): Boolean = false
}