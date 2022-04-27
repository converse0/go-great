package com.masuta.gogreat.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.gender
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
import com.masuta.gogreat.presentation.profile.ProfileViewModel
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import com.masuta.gogreat.presentation.workout.StartTrainingScreen
import com.masuta.gogreat.presentation.workout.WorkoutScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            SportTheme {
                Navigation(
                    listOf(
                        BottomNavigationItem(
                            route = "main",
                            icon = R.drawable.ic_house,
                            title = "Main Page"
                        ),
                        BottomNavigationItem(
                            route = "diet",
                            icon = R.drawable.ic_fork_spoon,
                            title = "My Diet"
                        ),
                        BottomNavigationItem(
                            route = "health",
                            icon = R.drawable.ic_heart_pulse,
                            title = "My Health"
                        ),
                        BottomNavigationItem(
                            route = "profile",
                            icon = R.drawable.ic_profile,
                            title = "Profile"
                        )
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
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

fun getSex(context: Context): Int {
    val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    return sharedPref.getInt("sex", 0)
}

@Composable
fun SetSex(context: Context, viewModel: ProfileViewModel, gender: MutableState<Int>) {

    viewModel.getParameters(gender = gender)
    when (gender.value) {
        0, 1 -> {
            val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("sex", gender.value)
            editor.apply()
        }
    }

}

@Composable
fun ChoseStartScreen(
    context: Context, viewModel: ProfileViewModel,
    startRouteName: MutableState<String>
) {
    val token = getToken(context)
    userToken = token
    val r = getTokenR(context)
    refreshUserToken = r
    val g = getSex(context)
    gender = g

    if (token.isEmpty()) {
        startRouteName.value = "launch-screen"
        return
    }
    val gender = remember {
        mutableStateOf(777)
    }
    SetSex(context, viewModel = viewModel, gender)
    if (viewModel.errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }
    when (gender.value) {
        -6 -> startRouteName.value = "sign-in"
        6 -> startRouteName.value = "about"
        777 -> {
            Toast
                .makeText(context.applicationContext,
                "unknown error",
                    Toast.LENGTH_LONG).show()
        }
        else -> {
            println("Gender $gender")
            startRouteName.value = "main"
        }
    }


}

@Composable
fun Navigation(items: List<BottomNavigationItem>) {
    val navController = rememberNavController()
    var selected by remember { mutableStateOf("main") }
    val startRouteName = remember { mutableStateOf("") }
    if (startRouteName.value.isEmpty()) {
        ChoseStartScreen(
            LocalContext.current,
            viewModel = hiltViewModel(),
            startRouteName = startRouteName
        )
    }
    NavHost(
        navController = navController,
        startDestination = startRouteName.value,
    ) {
        composable(route = "main") {
            MainScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                menuItems = items,
                selected = selected,
                onSelect = { selected = it })
        }

        composable(route = "new-training") {
            NewTrainingScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = "launch-training") {
            LaunchTrainingScreen()
        }
        composable(route = "profile") {
            ProfileScreen(
                viewModel = hiltViewModel(),
                navController = navController,
                menuItems = items,
                selected = selected,
                onSelect = { selected = it })
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
            DietScreen(
                navController = navController,
                menuItems = items,
                selected = selected,
                onSelect = { selected = it })
        }
        composable(route = "health") {
            HealthScreen(
                navController = navController,
                menuItems = items,
                selected = selected,
                onSelect = { selected = it })
        }
        composable(route = "list-exercise/{typeId}") {
            ExerciseScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                typeId = it.arguments?.getString("typeId")
            )
        }
        composable(route = "workout/{uid}") {
            WorkoutScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                uid = it.arguments?.getString("uid")
            )
        }
        composable(route = "start-training/{uid}") {
            StartTrainingScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                uid = it.arguments?.getString("uid")
            )
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val icon: Int,
)


