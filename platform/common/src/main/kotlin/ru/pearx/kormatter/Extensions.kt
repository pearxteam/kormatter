/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter


/*
 * Created by mrAppleXZ on 12.08.18.
 */
fun Formatter.format(str: String, vararg args: Any?) = formatTo(StringBuilder(), str, args).toString()
fun String.format(vararg args: Any?) = DefaultFormatter.format(this, *args)