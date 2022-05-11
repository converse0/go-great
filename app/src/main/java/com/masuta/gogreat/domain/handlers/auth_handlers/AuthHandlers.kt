package com.masuta.gogreat.domain.handlers.auth_handlers

data class AuthHandlers(
    val getToken: GetToken,
    val signin: SignIn,
    val signup: SignUp
)
