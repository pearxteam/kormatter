/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils


/*
 * Created by mrAppleXZ on 04.08.18.
 */
internal fun String.intNonEmpty() = if (isEmpty()) null else toInt()
internal fun String.singleNonEmpty() = if (isEmpty()) null else single()
internal operator fun MatchResult.get(v: String): String = (this.groups as MatchNamedGroupCollection)[v]?.value ?: ""