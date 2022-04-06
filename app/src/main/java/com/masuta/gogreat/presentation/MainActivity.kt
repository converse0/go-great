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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import com.masuta.gogreat.presentation.auth.LaunchScreen
import com.masuta.gogreat.presentation.auth.SignInScreen
import com.masuta.gogreat.presentation.auth.SignUpScreen
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
                        BottomNavigationItem(route = "new-training", icon = Icons.Default.Add),
                        BottomNavigationItem(route = "launch-training", icon = Icons.Default.PlayArrow),
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
        return "sign-in"
    }
    return "main"
}

@Composable
fun Navigation(items: List<BottomNavigationItem>) {
    val navController = rememberNavController()
    var selected by remember { mutableStateOf("main") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color.LightGray
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items.forEach { item ->
                            IconButton(onClick = {
                                navController.navigate(item.route)
                                selected = item.route
                            }) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.route,
                                    tint = if (item.route == selected) Color.Green else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        ) {
            NavHost(navController = navController, startDestination =choseStartScreen(LocalContext.current)) {
                composable(route = "main") {
                    MainScreen()
                }
                composable(route = "new-training") {
                    NewTrainingScreen()
                }
                composable(route = "launch-training") {
                    LaunchTrainingScreen()
                }
                composable(route = "profile") {
                    ProfileScreen(viewModel = viewModel())
                }
                composable(route = "sign-in") {
                    SignInScreen(viewModel = viewModel(), navController = navController)
                }
                composable(route = "sign-up") {
                    SignUpScreen(viewModel = viewModel(), navController = navController)
                }
                composable(route = "launch-screen") {
                    LaunchScreen(navController = navController)
                }
            }
        }
    }
}

data class BottomNavigationItem (
    val route: String,
    val icon: ImageVector,
        )

@Preview
@Composable
fun NavigationPreview() {
    SportTheme {
        NewTrainingScreen()
    }
}


