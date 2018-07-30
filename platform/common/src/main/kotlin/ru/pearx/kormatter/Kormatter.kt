package ru.pearx.kormatter

/*
 * Created by mrAppleXZ on 08.07.18 14:43
 */
fun String.format(vararg params: Any?) = Formatter().format(this, params)