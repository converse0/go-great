package com.masuta.gogreat.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedSplashScreen(
    navController: NavHostController,
    startRouteName: String
) {

    var count by remember { mutableStateOf(0) }

    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2500
        )
    )
    if (startRouteName != "launch-screen") {
        startAnimation = true
        count++
        if (count == 1) {
            launch(
                navController = navController,
                startRouteName = startRouteName
            )
        }
    }
    Splash(alphaAnim = alphaAnim.value)
}

fun launch(
    navController: NavHostController,
    startRouteName: String
) {
    println("RECOMPOSE $startRouteName")
    CoroutineScope(Dispatchers.IO).launch {
        delay(3000)
        CoroutineScope(Dispatchers.Main).launch {
            navController.navigate(startRouteName)
        }
    }
}

@Composable
fun Splash(
    alphaAnim: Float
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(250.dp).alpha(alphaAnim)
        )
    }
}