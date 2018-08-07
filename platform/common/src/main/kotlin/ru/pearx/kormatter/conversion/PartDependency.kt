/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion

import ru.pearx.kormatter.parser.FormatString


/*
 * Created by mrAppleXZ on 06.08.18.
 */
enum class PartDependency(private val shouldPartExist: Boolean?)
{
    REQUIRED(true),
    OPTIONAL(null),
    FORBIDDEN(false);

    fun check(value: Any?): Boolean
    {
        return (shouldPartExist == null) || (shouldPartExist == (value != null))
    }

    fun getErrorMessage(str: FormatString, valueName: String): String
    {
        if(shouldPartExist == null)
            return ""

        return "'$str' should${if(shouldPartExist) "" else "n't"} have the $valueName."
    }

    inline fun checkAndThrow(str: FormatString, value: Any?, valueName: String, thrower: (String) -> Throwable)
    {
        if(!check(value))
            throw thrower(getErrorMessage(str, valueName))
    }
}