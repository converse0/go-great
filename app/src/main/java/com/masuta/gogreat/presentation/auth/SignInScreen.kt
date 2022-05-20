package com.masuta.gogreat.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    navController.navigate("launch-screen")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Log in",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignInForm(viewModel = viewModel, navController = navController)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInForm(
    viewModel: SignInViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isEnabledButton = remember { mutableStateOf(true) }

    InputTextField(
        text = "Email",
        value = email,
        keyboardController = keyboardController,
        onChangeValue = { email = it},
    )
    Spacer(modifier = Modifier.height(16.dp))
    InputTextField(
        text = "Password",
        value = password,
        isPassword = true,
        keyboardController = keyboardController,
        onChangeValue = { password = it },
    )
    val focusManager = LocalFocusManager.current

    MainTextButton(
        text = "Login",
        color = Red,
        enabled = isEnabledButton.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        isEnabledButton.value = false
        val user = User(email = email, password = password)
focusManager.clearFocus()
        CoroutineScope(Dispatchers.IO).launch {
            val resp = viewModel.signIn(user)

            withContext(Dispatchers.Main) {
                if(resp["status"] as Boolean){
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
            isEnabledButton.value = true
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account? "
        )
        Text(
            text = "Sing up.",
            color = Color.Gray,
            modifier = Modifier
                .clickable {
                    navController.navigate("sign-up")
                }
        )
    }
}