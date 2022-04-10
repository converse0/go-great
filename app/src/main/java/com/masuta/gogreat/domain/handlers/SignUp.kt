package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class SignUp(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Boolean {
        return repository.signup(user)
    }
}