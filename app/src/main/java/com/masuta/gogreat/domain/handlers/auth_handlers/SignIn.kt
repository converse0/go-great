package com.masuta.gogreat.domain.handlers.auth_handlers

import com.masuta.gogreat.core.store.AuthStore
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class SignIn(
    private val repository: AuthRepository,
    private val store: AuthStore
) {

    suspend operator fun invoke(user: User): Map<String, Any?> {
        val resp = repository.login(user)
        val token = resp.getOrDefault("loginResponse", defaultValue = {null})
        if (token!=null) {
            store.setLocalToken(token as LoginResponse)
        }
        return resp
    }
}