/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion.elements.base

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.ArgumentIndexHolder
import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 10.08.18.
 */
class Conversion private constructor(
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        private val formatter: (FormatString, Appendable, Any?) -> Unit
): ConversionBase()
{
    constructor(
            precisionDependency: PartDependency = PartDependency.OPTIONAL,
            widthDependency: PartDependency = PartDependency.OPTIONAL,
            formatter: (FormatString, Any?) -> String
    ) : this(precisionDependency, widthDependency, { str, to, arg -> to.append(formatter(str, arg)) })

    override val canTakeArguments: Boolean
        get() = true

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, to: Appendable, vararg args: Any?)
    {
        formatter(str, to, takeArgument(str, indexHolder, *args))
    }
}