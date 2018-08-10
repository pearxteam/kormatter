/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion.elements

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.ArgumentIndexHolder
import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 04.08.18.
 */
interface IConversion
{
    val widthDependency: PartDependency

    val precisionDependency: PartDependency

    val canTakeArguments: Boolean

    fun <T : Appendable> format(str: FormatString, indexHolder: ArgumentIndexHolder, to: T, vararg args: Any?): T

    fun check(str: FormatString)
}