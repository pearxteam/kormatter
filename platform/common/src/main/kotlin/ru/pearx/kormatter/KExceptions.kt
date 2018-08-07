/*
 * Copyright © 2018 mrAppleXZ
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ru.pearx.kormatter


/*
 * Created by mrAppleXZ on 02.08.18.
 */

open class IllegalPrecisionException : RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
}

open class IllegalWidthException : RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
}

open class IllegalFlagsException : RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
}

open class IllegalConversionException : RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
}

open class ConversionAlreadyExistsException : RuntimeException
{
    constructor() : super()
    constructor(message: String?): super(message)
}