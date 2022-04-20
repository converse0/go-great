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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.ui.theme.Red
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavHostController) {
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
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
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
fun SignUpForm(viewModel: SignUpViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp)
        ) {
            item {
                InputTextField(
                    text = "UserName",
                    value = username,
                    keyboardController = keyboardController,
                    onChangeValue = { username = it},
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                    keyboardController = keyboardController,
                    onChangeValue = { password = it},
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    text = "Confirm password",
                    value = passwordConfirm,
                    keyboardController = keyboardController,
                    onChangeValue = { passwordConfirm = it},
                )
                Spacer(modifier = Modifier.height(16.dp))
//    var checked by remember { mutableStateOf(false) }
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Checkbox(checked = checked, onCheckedChange = { checked = !checked })
//        Text(
//            text = "Remember me"
//        )
//    }
            }
        }
        TextButton(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val resp = viewModel.signUp(username, email,password,passwordConfirm)
                    if (resp) {
                        val res = viewModel.signIn(User(email = email, password = password))
                        if (res["status"] as Boolean) {
                            viewModel.setToken(context = context, token = res["loginResponse"] as LoginResponse?)
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
                    } else {
                        Toast.makeText(
                            context,
                            "Sign up failed, fill all fields, and enter the same password twice",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Red, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 40.dp,
                    bottom = 10.dp
                )
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Sign Up",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }

    Text(
        text = "By signing up, you agree to our Privacy Policy"
    )
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SportTheme() {
        SignUpScreen(viewModel = viewModel(), navController = NavHostController(LocalContext.current))
    }
}