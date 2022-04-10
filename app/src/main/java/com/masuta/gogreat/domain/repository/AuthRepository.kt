package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.domain.model.User

interface AuthRepository {

    suspend fun login(user: User): Map<String, Any?>

    suspend fun signup(user: User): Boolean

}