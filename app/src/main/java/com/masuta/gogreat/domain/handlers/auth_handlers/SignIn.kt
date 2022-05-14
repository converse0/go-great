package com.masuta.gogreat.domain.handlers.auth_handlers

import com.masuta.gogreat.core.service.auth_service.AuthService
import com.masuta.gogreat.domain.model.User

class SignIn(
    private val authService: AuthService
) {
    suspend operator fun invoke(email: String, password: String): Map<String, Any?> {
        val user = User(email = email, password = password)
        return authService.signin(user)
    }
}