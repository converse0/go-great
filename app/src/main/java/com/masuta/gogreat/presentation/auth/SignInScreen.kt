package com.masuta.gogreat.presentation.auth

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(viewModel: SignInViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SignInForm(viewModel, navController)
    }
}

@Composable
fun SignInForm(viewModel: SignInViewModel, navController: NavHostController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    OutlinedTextField(value = email, onValueChange ={email = it} )
    var password by remember { mutableStateOf("") }
    OutlinedTextField(value = password, onValueChange ={password = it} )
    OutlinedButton(onClick = {
        val user = User(email=email, password=password)
        CoroutineScope(Dispatchers.Main).launch {
            val resp = viewModel.signIn(user)
            if(resp.first){
                viewModel.setToken(context = context, token = resp.second)
                navController.navigate("main")
            }
        }
    }) {
        Text("Sign In")

    }
}