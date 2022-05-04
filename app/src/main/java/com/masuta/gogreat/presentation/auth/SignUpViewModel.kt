package com.masuta.gogreat.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import com.masuta.gogreat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

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

   suspend fun signUp(
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

    suspend fun signIn(user: User): Map<String, Any?> {
        val resp = repository.login(user)
        val token = resp["loginResponse"] as LoginResponse
        repository.setLocalToken(token)

        return resp
    }
}