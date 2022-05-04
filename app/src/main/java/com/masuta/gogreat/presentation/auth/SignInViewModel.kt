package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    suspend fun signIn(user: User): Map<String, Any?> {
        val resp = repository.login(user)
        val token = resp["loginResponse"] as LoginResponse

        repository.setLocalToken(token)
        return resp
    }
}