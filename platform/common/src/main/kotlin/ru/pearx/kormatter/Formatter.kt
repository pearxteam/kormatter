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

        private val REGEX: Regex = Regex("""%(?:(\d+)\$)?([-#+ 0,(<]+)?(\d+)?(?:\.(\d+))?(.)((?<=t).)?""")
    }

    fun format(format: String, vararg args: Any?): String
    {
        return REGEX.replace(format)
        { res ->
            val arg_ind = res.groupValues[1]
            val flags = res.groupValues[2]
            val width = res.groupValues[3]
            val precision = res.groupValues[4]
            val conversion = res.groupValues[5] as? Char
            val conversion_dt = res.groupValues[6] as? Char

            when (conversion)
            {
                else ->
                {
                    ""
                }
            }
        }
    }
}