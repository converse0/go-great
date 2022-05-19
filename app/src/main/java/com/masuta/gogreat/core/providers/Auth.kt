package com.masuta.gogreat.core.providers

import com.masuta.gogreat.core.model.User

interface Auth {

    suspend fun login(user: User): Map<String, Any?>

    suspend fun signup(user: User): Boolean

}