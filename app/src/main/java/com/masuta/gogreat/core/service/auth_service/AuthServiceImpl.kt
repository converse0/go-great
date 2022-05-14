package com.masuta.gogreat.core.service.auth_service

import com.masuta.gogreat.core.store.AuthStore
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class AuthServiceImpl(
    private val repository: AuthRepository,
    private val store: AuthStore
): AuthService {

    private fun checkUsername(username: String?): Boolean {
        return when(username) {
            "" -> false
            null -> false
            else -> true
        }
    }

    private fun checkEmail(email: String?): Boolean {
        return when (email) {
            "" -> false
            null -> false
            else -> true
        }
    }
    private fun checkPassword(password: String?): Boolean {
        return when (password) {
            "" -> false
            null -> false
            else -> true
        }
    }
    private fun checkPasswordConfirm(password: String?, passwordConfirm: String?): Boolean {
        return when (password) {
            passwordConfirm -> true
            else -> false
        }
    }

    override suspend fun signin(user: User): Map<String, Any?> {
        val resp = repository.login(user)
        val token = resp.getOrDefault("loginResponse", defaultValue = {null})
        if (token != null) {
            store.setLocalToken(token as LoginResponse)
        }
        return resp
    }

    override suspend fun signup(user: User): Boolean {
        if (checkUsername(user.username) && checkEmail(user.email) &&
            checkPassword(user.password) && checkPasswordConfirm(user.password, user.passwordConfirm)
        ) {
            val resp = repository.signup(user)
            if(resp) {
                return true
            }
        }

        return false
    }

}