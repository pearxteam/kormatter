/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils.internal

/*
 * Created by mrAppleXZ on 07.08.18.
 */
internal actual val lineSeparator: String
    get() = "\n"

internal actual fun CharSequence.lengthSequence(length: Int): CharSequence = subSequence(0, length)