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
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    suspend fun signIn(user: User): Map<String, Any?> {
        return repository.login(user)
    }

    fun setToken(context: Context, token: LoginResponse?) {

        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("accessToken", token!!.accessToken)
        editor.putString("refreshToken", token.refreshToken)

        userToken = token.accessToken
        refreshUserToken = token.refreshToken
        editor.apply()

    }
}