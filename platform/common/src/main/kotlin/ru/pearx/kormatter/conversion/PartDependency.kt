/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.conversion


/*
 * Created by mrAppleXZ on 06.08.18.
 */
enum class PartDependency(val shouldPartExist: Boolean?)
{
    REQUIRED(true),
    OPTIONAL(null),
    FORBIDDEN(false);

    fun check(value: Any?): Boolean
    {
        return (shouldPartExist == null) || (shouldPartExist == (value != null))
    }
}