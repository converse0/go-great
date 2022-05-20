package com.masuta.gogreat.core.handlers.auth_handlers

import com.masuta.gogreat.core.handlers.ErrorHandler

data class AuthHandlers(
    val getToken: GetToken,
    val signin: SignIn,
    val signup: SignUp,
    val errorHandler: ErrorHandler
)
