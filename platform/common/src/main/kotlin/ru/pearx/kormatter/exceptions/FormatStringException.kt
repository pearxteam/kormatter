package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
abstract class FormatStringException : RuntimeException
{
    protected abstract val localMessage: String
    val formatString: FormatString

    constructor(formatString: FormatString) : super()
    {
        this.formatString = formatString
    }

    constructor(formatString: FormatString, cause: Throwable?) : super(cause)
    {
        this.formatString = formatString
    }

    override val message: String?
        get() = "$formatString: $localMessage"
}