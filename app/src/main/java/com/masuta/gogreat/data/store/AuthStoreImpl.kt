package com.masuta.gogreat.data.store

import android.content.Context
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import javax.inject.Inject

class AuthStoreImpl @Inject constructor(
    private val context: Context
) : AuthStore {

    private val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    override suspend fun getLocalToken(): String? {
        val value = sharedPref.getString("accessToken", "")
        return when (value) {
            "" -> null
            else -> value
        }
    }

    override suspend fun getLocalRefreshToken(): String? {
        val value = sharedPref.getString("refreshToken", "")
        return when (value) {
            "" -> null
            else -> value
        }
    }

    override suspend fun setLocalToken(token: LoginResponse?) {
        val editor = sharedPref.edit()
        editor.putString("accessToken", token!!.accessToken)
        editor.putString("refreshToken", token.refreshToken)

        userToken = token.accessToken
        refreshUserToken = token.refreshToken

        editor.apply()
    }

}