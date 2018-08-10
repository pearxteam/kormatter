package ru.pearx.kormatter.exceptions

import ru.pearx.kormatter.conversion.PartDependency
import ru.pearx.kormatter.utils.parser.FormatString


/*
 * Created by mrAppleXZ on 09.08.18.
 */
open class PartDependencyException(formatString: FormatString, val dependency: PartDependency, private val name: String) : FormatStringException(formatString)
{
    override val localMessage: String
        get()
        {
            val b = dependency.shouldPartExist ?: return ""
            return "The format string should${if(b) "" else "n't"} have the $name."
        }
}