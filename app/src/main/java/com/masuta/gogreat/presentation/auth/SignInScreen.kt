package com.masuta.gogreat.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "Log in",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignInForm(viewModel = viewModel, navController = navController)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInForm(viewModel: SignInViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("email") }
    InputTextField(
        text = "Email",
        value = email,
        keyboardController = keyboardController,
        onChangeValue = { email = it},
    )
    Spacer(modifier = Modifier.height(16.dp))
    var password by remember { mutableStateOf("password") }
    InputTextField(
        text = "Password",
        value = password,
        keyboardController = keyboardController,
        onChangeValue = { password = it },
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        var checked by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = checked, onCheckedChange = { checked = !checked })
            Text(
                text = "Remember me"
            )
        }
        Text(
            text = "Forgot password?",
            modifier = Modifier
                .clickable {
//                    navController.navigate("")
                }
        )
    }
    TextButton(
        onClick = {
            val user = User(email=email, password=password)
            CoroutineScope(Dispatchers.Main).launch {
                val resp = viewModel.signIn(user)
                if(resp["status"] as Boolean){

                    viewModel.setToken(context = context, token = resp["loginResponse"] as LoginResponse?)
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
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Text(
            text = "Login",
            color = Color.White,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(vertical = 16.dp)
        )
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

@Preview
@Composable
fun SignInScreenPreview() {
    SportTheme() {
        SignInScreen(viewModel = viewModel(), navController = NavHostController(context = LocalContext.current))
    }
}