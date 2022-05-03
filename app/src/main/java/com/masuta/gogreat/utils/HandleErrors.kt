package com.masuta.gogreat.utils

sealed class Errors {
    data class Unknown(val code: Int, val route: String): Errors()
    data class NotFound(val code: Int, val route: String): Errors()
    data class Internal(val code: Int, val route: String): Errors()
    data class Unauthenticated(val code: Int, val route: String): Errors()
    data class Timeout(val code: Int, val route: String = ""): Errors()
}

fun handleErrors(
    code: Int,
): Errors {
   return when(code) {
        16 -> Errors.Unauthenticated(16, "sing-in")
        2 -> Errors.Unknown(2, "about")
        5 -> Errors.NotFound(5, "about")
        13 -> Errors.Internal(13, "about")
        else -> Errors.Timeout(777)
   }
}