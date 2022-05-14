package com.masuta.gogreat.domain.handlers.auth_handlers

import com.masuta.gogreat.core.store.AuthStore
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken

class GetToken(
    private val store: AuthStore
) {

    suspend operator fun invoke() {
        val token = store.getLocalToken()
        val refreshToken = store.getLocalRefreshToken()

        userToken = token
        refreshUserToken = refreshToken
    }
}