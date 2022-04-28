package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.ui.theme.Red

@Composable
fun LaunchScreen(
    navController: NavHostController
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.index),
            contentDescription = "Launch Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(20.dp)
        ) {
            Text(
                text = "Hello!",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Login or sign up to create your work out plan.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(Modifier.height(20.dp))
            MainTextButton(
                text = "Login",
                color = Red,
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate("sign-in")
            }
            Spacer(Modifier.height(20.dp))
            MainTextButton(
                text = "Sign up",
                color = Color.White,
                textColor = Color.Black,
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate("sign-up")
            }
        }
    }
}
