package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun LaunchScreen(
//    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        Box(
          modifier = Modifier
              .padding(vertical = 60.dp)
              .size(200.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(color = Color.Gray, shape = RoundedCornerShape(16.dp))
        )
        Text(
            text = "Hello!",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.W400
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Login or sign up to create your work out plan.",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(20.dp))
        TextButton(
            onClick = {
                // navController.navigate('sign-in')
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login", color = Color.White, modifier = Modifier.padding(vertical = 16.dp))
        }
        Spacer(Modifier.height(20.dp))
        TextButton(
            onClick = {
                      // navController.navigate('sign-up')
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign up",
                color = Color.White,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun LaunchScreenPreview() {
    SportTheme {
        LaunchScreen()
    }
}