package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {

        SignUpForm(viewModel, navController)
    }
}

@Composable
fun SignUpForm(viewModel: SignUpViewModel, navController: NavHostController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    OutlinedTextField(value = email, onValueChange ={email = it} )
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    OutlinedTextField(value = password, onValueChange ={password = it} )
    OutlinedTextField(value = passwordConfirm, onValueChange ={passwordConfirm = it} )
    OutlinedButton(onClick = {
        CoroutineScope(Dispatchers.Main).launch {
            val resp = viewModel.signUp(email,password,passwordConfirm)
            if (resp) {
                navController.navigate("sign-in")
        }
    }}) {
        Text("Sign Up")
    }
}