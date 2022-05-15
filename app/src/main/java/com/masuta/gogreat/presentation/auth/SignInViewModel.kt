package com.masuta.gogreat.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.presentation.profile.routeTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authHandlers: AuthHandlers,
): ViewModel() {

    fun signIn(
        email: String,
        password: String,
        navController: NavHostController,
        context: Context
    ) {
        viewModelScope.launch {
            val resp = authHandlers.signin(email, password)

            if(resp["status"] as Boolean){
                withContext(Dispatchers.Main) {
                    routeTo(navController, "main")
                }
            } else {
                resp["message"]?.let {
                    Toast.makeText(
                        context,
                        it as String,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}