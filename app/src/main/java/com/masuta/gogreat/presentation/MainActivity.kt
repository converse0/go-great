package com.masuta.gogreat.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import com.masuta.gogreat.presentation.auth.AboutScreen
import com.masuta.gogreat.presentation.auth.LaunchScreen
import com.masuta.gogreat.presentation.auth.SignInScreen
import com.masuta.gogreat.presentation.auth.SignUpScreen
import com.masuta.gogreat.presentation.diet.DietScreen
import com.masuta.gogreat.presentation.health.HealthScreen
import com.masuta.gogreat.presentation.launch_training.LaunchTrainingScreen
import com.masuta.gogreat.presentation.main.MainScreen
import com.masuta.gogreat.presentation.new_training.NewTrainingScreen
import com.masuta.gogreat.presentation.profile.ProfileScreen
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportTheme {
                Navigation(
                    listOf(
                        BottomNavigationItem(route = "main", icon = Icons.Default.Home),
                        BottomNavigationItem(route = "diet", icon = Icons.Default.Add),
                        BottomNavigationItem(route = "health", icon = Icons.Default.PlayArrow),
                        BottomNavigationItem(route = "profile", icon = Icons.Default.Person)
                    )
                )
            }
        }
    }
}

fun getToken(context: Context): String {
    val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    return sharedPref.getString("accessToken", "")!!
}

fun getTokenR(context: Context): String {
    val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    return sharedPref.getString("refreshToken", "")!!
}

fun choseStartScreen(context: Context): String {
       val token = getToken(context)
    userToken=token
    val r = getTokenR(context)
    refreshUserToken = r

    if (token.isEmpty()) {
        return "launch-screen"
    }
    return "main"
}

@Composable
fun Navigation(items: List<BottomNavigationItem>) {
    val navController = rememberNavController()
    var selected by remember { mutableStateOf("main") }

    NavHost(
        navController = navController,
        startDestination = choseStartScreen(LocalContext.current)
    ) {
        composable(route = "main") {
            MainScreen(navController = navController, menuItems = items, selected = selected, onSelect = { selected = it })
        }
        composable(route = "new-training") {
            NewTrainingScreen(navController = navController, menuItems = items, selected = selected, onSelect = { selected = it })
        }
        composable(route = "launch-training") {
            LaunchTrainingScreen()
        }
        composable(route = "profile") {
            ProfileScreen(viewModel = hiltViewModel(), navController = navController, menuItems = items, selected = selected, onSelect = { selected = it })
        }
        composable(route = "sign-in") {
            SignInScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable(route = "sign-up") {
            SignUpScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable(route = "launch-screen") {
            LaunchScreen(navController = navController)
        }
        composable(route = "about") {
            AboutScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable(route = "diet") {
            DietScreen(navController = navController, menuItems = items, selected = selected, onSelect = { selected = it })
        }
        composable(route = "health") {
            HealthScreen(navController = navController, menuItems = items, selected = selected, onSelect = { selected = it })
        }
    }
}

data class BottomNavigationItem (
    val route: String,
    val icon: ImageVector,
)


