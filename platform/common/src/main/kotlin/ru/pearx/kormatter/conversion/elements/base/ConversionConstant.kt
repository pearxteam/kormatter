/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion.elements.base

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString
import ru.pearx.kormatter.utils.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 07.08.18.
 */
class ConversionConstant(
        private val replacement: String,
        override val widthDependency: PartDependency = PartDependency.OPTIONAL,
        override val precisionDependency: PartDependency = PartDependency.OPTIONAL
) : ConversionBase()
{
    override val canTakeArguments: Boolean
        get() = false

    override fun format(str: FormatString, indexHolder: ArgumentIndexHolder, to: Appendable, vararg args: Any?)
    {
        to.append(replacement)
    }
}