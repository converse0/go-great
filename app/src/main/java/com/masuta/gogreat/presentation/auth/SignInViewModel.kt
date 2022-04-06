package com.masuta.gogreat.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import com.masuta.gogreat.domain.use_case.Authorization

class SignInViewModel: ViewModel() {

    private val authorization = Authorization()

    suspend fun signIn(user: User): Map<String, Any?> {
        return authorization.login(user)
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