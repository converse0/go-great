package com.masuta.gogreat.core.store

import com.masuta.gogreat.domain.model.LoginResponse

interface AuthStore {

    suspend fun getLocalToken(): String?
    suspend fun getLocalRefreshToken(): String?

    suspend fun setLocalToken(token: LoginResponse?)

}