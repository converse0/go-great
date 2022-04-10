package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class Login(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Map<String, Any?> {
        return repository.login(user)
    }
}