package com.masuta.gogreat.core.service.auth_service

import com.masuta.gogreat.core.model.User

interface AuthService {

    suspend fun signin(user: User): Map<String, Any?>

    suspend fun signup(user: User): Boolean

    suspend fun getToken()

}