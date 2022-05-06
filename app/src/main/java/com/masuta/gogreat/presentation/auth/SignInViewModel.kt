package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.handlers.SignIn
import com.masuta.gogreat.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signin: SignIn
): ViewModel() {

    suspend fun signIn(user: User): Map<String, Any?> {
        return signin(user)
    }
}