package com.masuta.gogreat.utils

fun String.validate(): String {
    return (this.filter { it.isDigit() }.toIntOrNull() ?: 30).toString()
}
