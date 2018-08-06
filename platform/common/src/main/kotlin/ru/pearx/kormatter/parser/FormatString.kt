/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.parser


/*
 * Created by mrAppleXZ on 05.08.18.
 */
data class FormatString(
        val argumentIndex: Int?,
        val flags: String,
        val width: Int?,
        val precision: Int?,
        val prepend: Char?,
        val conversion: Char,
        val start: Int,
        val endInclusive: Int
)