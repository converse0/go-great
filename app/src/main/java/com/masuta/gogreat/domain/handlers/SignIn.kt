package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.data.store.AuthStore
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class SignIn(
    private val repository: AuthRepository,
    private val store: AuthStore
) {

    suspend operator fun invoke(user: User): Map<String, Any?> {
        val resp = repository.login(user)
        val token = resp["loginResponse"] as LoginResponse

        store.setLocalToken(token)
        return resp
    }
}