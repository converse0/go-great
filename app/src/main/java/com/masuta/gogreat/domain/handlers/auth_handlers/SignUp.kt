package com.masuta.gogreat.domain.handlers.auth_handlers

import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository

class SignUp(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String?,
        email: String?,
        password: String?,
        passwordConfirm: String?
    ): Boolean {
        if (checkUsername(username) && checkEmail(email) &&
            checkPassword(password) && checkPasswordConfirm(password, passwordConfirm)
        ) {
            val user = User(username!!, email!!, password!!)
            val resp = repository.signup(user)

            if(resp) {
                return true
            }
        }

        return false
    }

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

}