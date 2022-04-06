package com.masuta.gogreat.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.use_case.Authorization

class SignInViewModel: ViewModel() {

    private val authorization = Authorization()

    suspend fun signIn(user: User): Pair<Boolean, String> {
        return authorization.login(user)
    }

    fun setToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.apply()
    }


}