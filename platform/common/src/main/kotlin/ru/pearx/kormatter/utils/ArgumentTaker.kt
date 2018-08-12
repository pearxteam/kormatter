/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter.utils

import ru.pearx.kormatter.FLAG_REUSE_ARGUMENT_INDEX
import ru.pearx.kormatter.exceptions.NoSuchArgumentException
import ru.pearx.kormatter.utils.internal.ArgumentIndexHolder


/*
 * Created by mrAppleXZ on 12.08.18.
 */
class ArgumentTaker internal constructor(private val holder: ArgumentIndexHolder, private val args: Array<out Any?>)
{
    internal lateinit var formatString: FormatString

    fun take(): Any?
    {
        when
        {
            //use explicit argument index
            formatString.argumentIndex != null ->
            {
                try
                {
                    holder.lastTaken = formatString.argumentIndex!! - 1
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException)
                {
                    throw NoSuchArgumentException(formatString, "Can't use the argument at index ${holder.lastTaken}!", e)
                }
            }
            //reuse previous argument index
            formatString.flags.contains(FLAG_REUSE_ARGUMENT_INDEX) ->
            {
                try
                {
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException)
                {
                    throw NoSuchArgumentException(formatString, "Can't reuse previously taken argument (${holder.lastTaken})!", e)
                }
            }
            //take the next argument
            else ->
            {
                holder.lastOrdinary++
                holder.lastTaken = holder.lastOrdinary
                try
                {
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException)
                {
                    throw NoSuchArgumentException(formatString, "Can't take the next ordinary argument (${holder.lastTaken})!", e)
                }
            }
        }
    }
}