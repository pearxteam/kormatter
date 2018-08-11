/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
abstract class FormatStringException : RuntimeException
{
    protected abstract val localMessage: String
    val formatString: FormatString

    constructor(formatString: FormatString) : super()
    {
        this.formatString = formatString
    }

    constructor(formatString: FormatString, cause: Throwable?) : super(cause)
    {
        this.formatString = formatString
    }

    override val message: String?
        get() = "$formatString: $localMessage"
}