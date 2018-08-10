/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class IllegalFormatArgumentException(
        formatString: FormatString,
        val argument: Any?,
        override val localMessage: String = "'$argument' is not a valid argument for this conversion."
) : FormatStringException(formatString)