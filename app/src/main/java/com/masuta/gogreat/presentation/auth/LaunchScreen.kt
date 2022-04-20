package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.masuta.gogreat.presentation.ui.theme.Red
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun LaunchScreen(
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Hello!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.W400
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Login or sign up to create your work out plan.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(20.dp))
        TextButton(
            onClick = {
                 navController.navigate("sign-in")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Red, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login", color = Color.White, modifier = Modifier.padding(vertical = 16.dp))
        }
        Spacer(Modifier.height(20.dp))
        TextButton(
            onClick = {
                navController.navigate("sign-up")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign up",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun LaunchScreenPreview() {
    SportTheme {
        LaunchScreen(navController = NavHostController(LocalContext.current))
    }
}