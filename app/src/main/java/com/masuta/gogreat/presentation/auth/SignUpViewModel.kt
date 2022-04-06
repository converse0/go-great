package com.masuta.gogreat.presentation.auth
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.use_case.Authorization
import kotlinx.coroutines.launch


class SignUpViewModel: ViewModel(){
    private val authorization = Authorization()

    private fun checkEmail(email: String?): Boolean {
        return when (email) {
            "" -> false
            null -> false
            else -> true
        }
    }
    private fun checkPassword(password: String?): Boolean {
        return when (password) {
            "" -> false
            null -> false
            else -> true
        }
    }
    private fun checkPasswordConfirm(password: String?, passwordConfirm: String?): Boolean {
        return when (password) {
            passwordConfirm -> true
            else -> false
        }
    }



   suspend fun signUp(email: String?, password: String?, passwordConfirm: String?): Boolean {
        if (checkEmail(email) &&
            checkPassword(password) &&
            checkPasswordConfirm(password,
                passwordConfirm)) {
            val user = User(email!!, password!!)

                val resp = authorization.signup(user)
                if(resp) {
                    return true
                }

        }
       return false

   }
}