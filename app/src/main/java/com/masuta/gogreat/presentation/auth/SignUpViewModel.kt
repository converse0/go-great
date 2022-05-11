package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.domain.handlers.auth_handlers.SignIn
import com.masuta.gogreat.domain.handlers.auth_handlers.SignUp
import com.masuta.gogreat.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authHandlers: AuthHandlers
) : ViewModel() {

   suspend fun signUp(
       username: String?,
       email: String?,
       password: String?,
       passwordConfirm: String?
   ): Boolean {
        return authHandlers.signup(username, email, password, passwordConfirm)
   }

    suspend fun signIn(user: User): Map<String, Any?> {
        return authHandlers.signin(user)
    }
}