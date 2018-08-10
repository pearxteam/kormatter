package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class IllegalFormatArgumentException(
        formatString: FormatString,
        val argument: Any?,
        override val localMessage: String = "'$argument' is not a valid argument for this conversion."
) : FormatStringException(formatString)