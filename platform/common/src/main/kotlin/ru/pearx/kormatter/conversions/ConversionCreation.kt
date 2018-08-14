/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversions

import ru.pearx.kormatter.utils.ArgumentTaker
import ru.pearx.kormatter.utils.FormatString
import ru.pearx.kormatter.utils.PartAction

/*
 * Created by mrAppleXZ on 12.08.18.
 */
fun conversion(replacement: String, widthAction: PartAction = PartAction.STANDARD, precisionAction: PartAction = PartAction.STANDARD) : Conversion
{
    return ConversionConstant(replacement, widthAction, precisionAction)
}

inline fun conversion(supportedFlags: CharArray = charArrayOf(), widthAction: PartAction = PartAction.STANDARD, precisionAction: PartAction = PartAction.STANDARD, crossinline executor: (str: FormatString, arg: Any?, to: Appendable) -> Unit) : Conversion
{
    return object : ConversionExecuting(supportedFlags, widthAction, precisionAction)
    {
        override fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker)
        {
            executor(str, taker.take(), to)
        }
    }
}

inline fun conversion(supportedFlags: CharArray = charArrayOf(), widthAction: PartAction = PartAction.STANDARD, precisionAction: PartAction = PartAction.STANDARD, crossinline executor: (str: FormatString, arg: Any?) -> String) : Conversion
{
    return object : ConversionExecuting(supportedFlags, widthAction, precisionAction)
    {
        override fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker)
        {
            to.append(executor(str, taker.take()))
        }
    }
}

inline fun conversionNotNull(supportedFlags: CharArray = charArrayOf(), widthAction: PartAction = PartAction.STANDARD, precisionAction: PartAction = PartAction.STANDARD, crossinline executor: (str: FormatString, arg: Any, to: Appendable) -> Unit) : Conversion
{
    return object : ConversionExecutingNotNull(supportedFlags, widthAction, precisionAction)
    {
        override fun formatTo(to: Appendable, str: FormatString, arg: Any)
        {
            executor(str, arg, to)
        }
    }
}

inline fun conversionNotNull(supportedFlags: CharArray = charArrayOf(), widthAction: PartAction = PartAction.STANDARD, precisionAction: PartAction = PartAction.STANDARD, crossinline executor: (str: FormatString, arg: Any) -> String) : Conversion
{
    return object : ConversionExecutingNotNull(supportedFlags, widthAction, precisionAction)
    {
        override fun formatTo(to: Appendable, str: FormatString, arg: Any)
        {
            to.append(executor(str, arg))
        }
    }
}