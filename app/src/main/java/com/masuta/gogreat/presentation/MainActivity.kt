package com.masuta.gogreat.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masuta.gogreat.R
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
import com.masuta.gogreat.presentation.new_training.ExerciseScreen
import com.masuta.gogreat.presentation.new_training.NewTrainingScreen
import com.masuta.gogreat.presentation.profile.ProfileScreen
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import com.masuta.gogreat.presentation.workout.StartTrainingScreen
import com.masuta.gogreat.presentation.workout.WorkoutScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportTheme {
                Navigation(
                    listOf(
                        BottomNavigationItem(route = "main", icon = R.drawable.ic_house, title = "Main Page"),
                        BottomNavigationItem(route = "diet", icon = R.drawable.ic_fork_spoon, title = "My Diet"),
                        BottomNavigationItem(route = "health", icon = R.drawable.ic_heart_pulse, title = "My Health"),
                        BottomNavigationItem(route = "profile", icon = R.drawable.ic_profile, title = "Profile")
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
            MainScreen(navController = navController,viewModel = hiltViewModel(),  menuItems = items, selected = selected, onSelect = { selected = it })
        }
        composable(route = "new-training") {
            NewTrainingScreen(navController = navController, viewModel = hiltViewModel())
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
        composable(route = "list-exercise/{typeId}") {
            ExerciseScreen(navController = navController, viewModel = hiltViewModel(), typeId = it.arguments?.getString("typeId"))
        }
        composable(route = "workout/{uid}") {
            WorkoutScreen(navController = navController, viewModel = hiltViewModel(), uid = it.arguments?.getString("uid"))
        }
        composable(route = "start-training/{exerciseId}") {
            StartTrainingScreen(navController = navController, viewModel = hiltViewModel(), exerciseId = it.arguments?.getString("exerciseId"))
        }
    }
}

data class BottomNavigationItem (
    val title: String,
    val route: String,
    val icon: Int,
)


