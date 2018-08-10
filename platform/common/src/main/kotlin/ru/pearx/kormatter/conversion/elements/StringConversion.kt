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
 * Created by mrAppleXZ on 10.08.18.
 */
open class StringConversion(
        private val formatter: (FormatString, Any?) -> String,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL
) : Conversion()
{
    override val canTakeArguments: Boolean
        get() = true

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, to: Appendable, vararg args: Any?)
    {
        to.append(formatter(str, takeArgument(str, indexHolder, *args)))
    }
}