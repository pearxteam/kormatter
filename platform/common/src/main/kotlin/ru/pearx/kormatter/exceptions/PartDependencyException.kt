/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class PartDependencyException(formatString: FormatString, val dependency: PartDependency, private val name: String) : FormatStringException(formatString)
{
    override val localMessage: String
        get()
        {
            val b = dependency.shouldPartExist ?: return ""
            return "The format string should${if(b) "" else "n't"} have the $name."
        }
}