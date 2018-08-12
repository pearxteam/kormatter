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
 * Created by mrAppleXZ on 04.08.18.
 */
interface Conversion
{
    val widthDependency: PartDependency

    val precisionDependency: PartDependency

    val canTakeArguments: Boolean

    fun format(str: FormatString, taker: ArgumentTaker, to: Appendable)

    fun check(str: FormatString)
}