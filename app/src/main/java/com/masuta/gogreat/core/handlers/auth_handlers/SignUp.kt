package com.masuta.gogreat.core.handlers.auth_handlers

import com.masuta.gogreat.core.service.auth_service.AuthService
import com.masuta.gogreat.core.model.User

class SignUp(
    private val authService: AuthService
) {
    suspend operator fun invoke(
        username: String?,
        email: String,
        password: String,
        passwordConfirm: String?
    ): Boolean {
        val user = User(username, email, password, passwordConfirm)
        return authService.signup(user)
    }
}