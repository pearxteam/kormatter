/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

import ru.pearx.kormatter.utils.FormatString


/*
 * Created by mrAppleXZ on 14.08.18.
 */
interface Formattable
{
    fun formatTo(to: Appendable, str: FormatString)
}