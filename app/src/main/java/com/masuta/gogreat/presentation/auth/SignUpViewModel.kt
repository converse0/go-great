package com.masuta.gogreat.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.presentation.profile.routeTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authHandlers: AuthHandlers
) : ViewModel() {

   fun signUp(
       username: String?,
       email: String,
       password: String,
       passwordConfirm: String?,
       navController: NavHostController,
       context: Context
   ) {
       viewModelScope.launch {
           val respSignUp = authHandlers.signup(username, email, password, passwordConfirm)

           if (respSignUp) {
               val respSingIn = authHandlers.signin(email = email, password = password)

               withContext(Dispatchers.Main) {
                   if (respSingIn["status"] as Boolean) {
                       withContext(Dispatchers.Main) {
                           routeTo(navController, "about")
                       }
                   } else {
                       respSingIn["message"]?.let {
                           Toast.makeText(
                               context,
                               it as String,
                               Toast.LENGTH_SHORT
                           ).show()
                       }
                   }
               }
           } else {
               withContext(Dispatchers.Main) {
                   Toast.makeText(
                       context,
                       "Sign up failed, fill all fields, and enter the same password twice",
                       Toast.LENGTH_SHORT
                   ).show()
               }
           }
       }
   }
}