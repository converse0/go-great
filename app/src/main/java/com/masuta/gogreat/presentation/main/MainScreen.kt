package com.masuta.gogreat.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.ui.theme.Red
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>,
    viewModel: MainViewModel
) {

    viewModel.clearLocalExercises()

    val context = LocalContext.current

    val listTrainings = remember { mutableStateOf(emptyList<Training>()) }
    val currentWorkout = remember { mutableStateOf(Training(
        name = "",
        exercises = mutableListOf(),
        interval = ""
    )) }
    val listPastTrainings = remember { mutableStateOf(emptyList<Training>()) }

    viewModel.getCurrentTraining(currentWorkout, context, navController)
    viewModel.getWorkouts(listTrainings, context, navController)
    viewModel.getPastTrainings(listPastTrainings, context, navController)

    Scaffold(
        bottomBar = {
            BottomMenuBar(
                navController = navController,
                selected = "main",
                onSelect = onSelect,
                menuItems = menuItems
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(20.dp)
                .padding(bottom = 60.dp)
        ) {
            Text(
                text = "Workouts",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.main_pic),
                            contentDescription = "Main Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    Text(
                        text = "Create your workout today according to your personal preferences",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    MainTextButton(
                        text = "Create new training",
                        color = Red,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        navController.navigate("new-training")
                    }

                    CurrentWorkoutSection(
                        navController = navController,
                        currentWorkout = currentWorkout)
                    WorkoutsSection(navController = navController,
                        listTrainings = listTrainings)
                    PastWorkoutsSection(navController = navController,
                        listPastTrainings = listPastTrainings)
                }
            }
        }
    }
}

@Composable
fun PastWorkoutsSection(
    navController: NavHostController,
    listPastTrainings: MutableState<List<Training>>,
) {

    if (listPastTrainings.value.isNotEmpty()) {
        Text(
            text = "Past workouts",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W300,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )
        WorkoutsList(
            workouts = listPastTrainings.value,
            navController = navController
        )
    }
}


@Composable
fun CurrentWorkoutSection(
    navController: NavHostController,
    currentWorkout: MutableState<Training>,
) {

    if (currentWorkout.value.name.isNotEmpty()) {
        Text(
            text = "Current workout",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W300,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )
        WorkoutItem(
            workout = currentWorkout.value,
            modifier = Modifier.fillMaxWidth(),
            onSelectItem = {
                navController.navigate("start-training/${currentWorkout.value.uid}")}
        )
    }
}

@Composable
fun WorkoutsSection(
    navController: NavHostController,
    listTrainings: MutableState<List<Training>>,
) {

    if (listTrainings.value.isNotEmpty()) {
        Text(
            text = "My workouts",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W300,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )
        WorkoutsList(workouts = listTrainings.value, navController = navController)
    }
}

@Composable
fun WorkoutsList(
    workouts: List<Training>,
    navController: NavHostController
) {
    LazyRow {
        items(workouts) { workout ->
            WorkoutItem(
                workout = workout,
                modifier = Modifier.width(250.dp),
                onSelectItem = { navController.navigate("workout/${it}") }
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutItem(
    workout: Training,
    modifier: Modifier = Modifier,
    onSelectItem: (String) -> Unit
) {
    val dateText = workout.date?.let {
        val date = it.split("T").get(0)
        val formatParse = SimpleDateFormat("yyyy-MM-dd")
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val dateParse = formatParse.parse(date)
        format.format(dateParse!!)
    } ?: "27 March 2019"

    if (workout.name.isEmpty()) {
        return
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.Gray,
        modifier = modifier
            .height(170.dp)
            .padding(horizontal = 20.dp)
            .clickable { workout.uid?.let { onSelectItem(it) } }
    ) {
        Box{
            workout.image?.let {
                GlideImage(imageModel = it,
                    modifier = Modifier.fillMaxWidth(),
                    colorFilter = ColorFilter.lighting(multiply = Color.Gray,add= Color.Black)
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.BottomStart)
            ) {
                val text = if(workout.name.isEmpty()) "No name" else workout.name
                Text(
                    text =text,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.W700
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontWeight = FontWeight.W300
                )
            }
        }
    }
}