package com.masuta.gogreat.utils



sealed class Errors(val errCode: Int, val errRoute: String)
class Unknown(val code: Int, val route: String): Errors(code, route)
class NotFound(val code: Int, val route: String): Errors(code, route)
class Internal(val code: Int, val route: String):Errors(code, route)
class Unauthenticated(val code: Int, val route: String): Errors(code, route)
class Timeout(val code: Int, val route: String = ""): Errors(code, route)

fun handleErrors(
    code: Int,
): Errors {
    return when(code) {
        16 -> Unauthenticated(16, "sing-in")
        2 -> Unknown(2, "about")
        5 -> NotFound(5, "about")
        13 -> Internal(13, "about")
        else -> Timeout(777)
    }
}