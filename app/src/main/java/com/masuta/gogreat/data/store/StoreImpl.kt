package com.masuta.gogreat.data.store

import android.content.Context
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import javax.inject.Inject

class StoreImpl @Inject constructor(
    private val context: Context
) : Store {

    private val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    override suspend fun getLocalCurrentExercise(): Int? {
        val value = sharedPref.getInt("currentExercise", -1)
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExercise(indexExercise: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExercise", indexExercise ?: -1)
        editor.apply()
    }

    override suspend fun getLocalCurrentExerciseSets(): Int? {
        val value = sharedPref.getInt("currentExerciseSets", -1)
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExerciseSets", exerciseSets ?: -1)
        editor.apply()
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