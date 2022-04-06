package com.masuta.gogreat.presentation.auth
import android.content.Context
import com.masuta.gogreat.domain.model.User


class SignUpViewModel {

    fun checkEmail(email: String?): Boolean {
        return when (email) {
            "" -> false
            null -> false
            else -> true
        }
    }
    fun checkPassword(password: String?): Boolean {
        return when (password) {
            "" -> false
            null -> false
            else -> true
        }
    }
    fun checkPasswordConfirm(password: String?, passwordConfirm: String?): Boolean {
        return when (password) {
            passwordConfirm -> true
            else -> false
        }
    }



    fun signUp(email: String?, password: String?, passwordConfirm: String?) {
        if (checkEmail(email) &&
            checkPassword(password) &&
            checkPasswordConfirm(password,
                passwordConfirm)) {
            val user = User(email!!, password!!)

        }
    }
}