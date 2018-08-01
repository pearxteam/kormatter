/*
 * Copyright © 2018 mrAppleXZ
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter

/*
 * Created by mrAppleXZ on 04.07.18 18:11
 */
open class Formatter
{
    companion object
    {
        val DEFAULT = Formatter()

        private val REGEX: Regex = Regex("""%(?:(\d+)\$)?([-#+ 0,(<]+)?(\d+)?(?:\.(\d+))?(.)([A-Za-z])?""")
        private val ARGUMENT_INDEX_GROUP = 1
        private val FLAGS_GROUP = 2
        private val WIDTH_GROUP = 3
        private val PRECISION_GROUP = 4
        private val CONVERSION_GROUP = 5
        private val DATETIME_CONVERSION_GROUP = 6
    }

    fun format(format: String, vararg args: Any?): String
    {
        return REGEX.replace(format)
        { res ->
            ""
        }
    }
}