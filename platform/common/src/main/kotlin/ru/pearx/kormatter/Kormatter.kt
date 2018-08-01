/*
 * Copyright © 2018 mrAppleXZ
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

/*
 * Created by mrAppleXZ on 08.07.18 14:43
 */
fun String.format(vararg params: Any?) = Formatter.DEFAULT.format(this, params)