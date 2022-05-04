package com.masuta.gogreat.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp),
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
                text = "Sign Up",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignUpForm(viewModel, navController)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpForm(
    viewModel: SignUpViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    val isEnabledButton = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            item {
                InputTextField(
                    text = "UserName",
                    value = username,
                    keyboardController = keyboardController,
                    onChangeValue = { username = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    text = "Email",
                    value = email,
                    keyboardController = keyboardController,
                    onChangeValue = { email = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    text = "Password",
                    value = password,
                    isPassword = true,
                    keyboardController = keyboardController,
                    onChangeValue = { password = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    text = "Confirm password",
                    value = passwordConfirm,
                    isPassword = true,
                    keyboardController = keyboardController,
                    onChangeValue = { passwordConfirm = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        MainTextButton(
            text = "Sign up",
            color = Red,
            enabled = isEnabledButton.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 40.dp,
                    bottom = 10.dp
                )
                .align(Alignment.BottomCenter)
        ) {
            isEnabledButton.value = false
            CoroutineScope(Dispatchers.IO).launch {
                val respSignUp = viewModel.signUp(username, email, password, passwordConfirm)

                if (respSignUp) {
                    val res = viewModel.signIn(User(email = email, password = password))
                    withContext(Dispatchers.Main) {
                        if (res["status"] as Boolean) {
                            navController.navigate("about")
                        } else {
                            res["message"]?.let {
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
                isEnabledButton.value = true
            }
        }
    }
    Text(
        text = "By signing up, you agree to our Privacy Policy"
    )
}