package com.masuta.gogreat.core.handlers.auth_handlers

import com.masuta.gogreat.core.service.auth_service.AuthService

class GetToken(
    private val authService: AuthService
) {

    suspend operator fun invoke() {
        authService.getToken()
    }
}