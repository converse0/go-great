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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.main.MainViewModel
import com.masuta.gogreat.presentation.profile.ProfileViewModel
import kotlinx.coroutines.*

@Composable
fun AnimatedSplashScreen(
    navController: NavHostController,
    startRouteName: MutableState<String>,
    viewModel: MainViewModel
) {
    val viewModelUser: ProfileViewModel = hiltViewModel()

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

    if (startRouteName.value == "launch-screen") {
        count += 1
        startAnimation = false
        if (count == 1) {
            launch(
                navController = navController,
                startRouteName = startRouteName.value,
                viewModel = null,
                viewModelUser = null
            )
        }
    } else if (startRouteName.value != "") {
        startAnimation = true
        Splash(alphaAnim = alphaAnim.value)
        count += 1
        if (count == 1) {
            launch(
                navController = navController,
                startRouteName = startRouteName.value,
                viewModel = viewModel,
                viewModelUser = viewModelUser
            )
        }
    }
}



fun launch(
    navController: NavHostController,
    startRouteName: String,
    viewModel: MainViewModel? = null,
    viewModelUser: ProfileViewModel? = null
) {
    CoroutineScope(Dispatchers.IO).launch {
        viewModelUser?.getUserParameters()
        viewModel?.getMyTrainings()
        viewModel?.getPastTrainings()
        viewModel?.getCurrentTraining()
        withContext(Dispatchers.Main) {
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
            modifier = Modifier
                .size(250.dp)
                .alpha(alphaAnim)
        )
    }
}