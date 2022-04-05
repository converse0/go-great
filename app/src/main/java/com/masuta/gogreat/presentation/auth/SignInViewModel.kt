package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.use_case.Authorization

class SignInViewModel: ViewModel() {
    suspend fun signIn(user: User): Boolean {
        val auth = Authorization()

        return auth.login(user)
    }
}