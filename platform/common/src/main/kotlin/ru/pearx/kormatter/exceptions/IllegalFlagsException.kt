package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class IllegalFlagsException(
        formatString: FormatString,
        override val localMessage: String
) : FormatStringException(formatString)