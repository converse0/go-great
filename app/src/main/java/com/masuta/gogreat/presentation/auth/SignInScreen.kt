package com.masuta.gogreat.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(viewModel: SignInViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        TextButton(onClick = { navController.navigate("sign-up") }) {
            Text(text = "Sign Up")
        }
        SignInForm(viewModel, navController)
    }
}

@Composable
fun SignInForm(viewModel: SignInViewModel, navController: NavHostController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("email") }
    OutlinedTextField(value = email,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    email = ""
                }
            },
        onValueChange ={email = it}
    )

    Spacer(modifier = Modifier.height(16.dp))
    var password by remember { mutableStateOf("password") }
    OutlinedTextField(value = password,
        onValueChange ={password = it},
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    password = ""
                }
            })
    OutlinedButton(onClick = {
        val user = User(email=email, password=password)
        CoroutineScope(Dispatchers.Main).launch {
            val resp = viewModel.signIn(user)
            if(resp["status"] as Boolean) {
                viewModel.setToken(context = context,
                    token = resp["loginResponse"] as LoginResponse?)
                navController.navigate("main")
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
    }) {
        Text("Sign In")

    }
}