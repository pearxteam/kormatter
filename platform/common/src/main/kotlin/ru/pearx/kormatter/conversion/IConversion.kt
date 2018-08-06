/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion


/*
 * Created by mrAppleXZ on 04.08.18.
 */
interface IConversion
{
    val character: Char

    val canHaveWidth: Boolean

    val canHavePrecision: Boolean
}