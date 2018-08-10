package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 10.08.18.
 */
open class NoSuchArgumentException(
        formatString: FormatString,
        override val localMessage: String,
        cause: Throwable?
) : FormatStringException(formatString, cause)