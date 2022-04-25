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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.ui.theme.Purple200
import com.masuta.gogreat.presentation.ui.theme.Red
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>,
    viewModel: MainViewModel
) {

    viewModel.clearLocalExercises()

    val listTrainings = remember { mutableStateOf(emptyList<Training>()) }
    val currentWorkout = remember { mutableStateOf(Training(
        name = "",
        exercises = mutableListOf(),
        interval = ""
    )) }
    val listPastTrainings = remember { mutableStateOf(emptyList<Training>()) }

    viewModel.getCurrentTraining(currentWorkout)
    viewModel.getWorkouts(listTrainings)
    viewModel.getPastTrainings(listPastTrainings)

    println("My workouts: ${listTrainings.value}")
    println("Current workouts: ${currentWorkout.value}")
    println("Past workouts: ${listPastTrainings.value}")

    val countCurrentWorkout = remember { mutableStateOf(0) }
    val countTotalWorkout = remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomMenuBar(navController = navController, selected = selected, onSelect = onSelect, menuItems = menuItems)
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
//                    TextButton(
//                        onClick = {
//                            navController.navigate("new-training")
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Red),
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
//                        Text(
//                            text = "Create new training",
//                            color = Color.White,
//                            modifier = Modifier.padding(vertical = 16.dp)
//                        )
//                    }

                    CurrentWorkoutSection(viewModel = viewModel,
                        navController = navController,
                        currentWorkout = currentWorkout, countCurrentWorkout)
                    WorkoutsSection(viewModel = viewModel, navController = navController,
                        listTrainings = listTrainings, countTotalWorkout)
                    PastWorkoutsSection(viewModel = viewModel, navController = navController,
                        listPastTrainings = listPastTrainings)
                }
            }
        }
    }
}

@Composable
fun PastWorkoutsSection(
    viewModel: MainViewModel,
    navController: NavHostController,
    listPastTrainings: MutableState<List<Training>>,
) {

//        viewModel.getPastTrainings(listPastTrainings)

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
    viewModel: MainViewModel,
    navController: NavHostController,
    currentWorkout: MutableState<Training>,
    countCurrentWorkout: MutableState<Int>
) {

//    if (countCurrentWorkout.value == 0) {
//        viewModel.getCurrentTraining(currentWorkout, countCurrentWorkout)
//    }
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
    viewModel: MainViewModel,
    navController: NavHostController,
    listTrainings: MutableState<List<Training>>,
    countTotalWorkout: MutableState<Int>
) {
//        viewModel.getExercises(listTrainings, countTotalWorkout)


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
        println("Date parse: ${dateParse}")
        format.format(dateParse!!)
    } ?: "27 March 2019"
//    val ymdDate = workout.date?.split("T")?.get(0) ?: ""
//    val dateText = SimpleDateFormat("dd MMMM yyyy").parse(ymdDate)

    if (workout.name.isEmpty()) {
        return
    }
//    println("Date: $ymdDate")
    Card(
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.Gray,
        modifier = modifier
//            .width(300.dp)
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
//                verticalArrangement = Arrangement.Bottom,
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
//            val internal = if(workout.interval.isEmpty()) "30s" else workout.interval
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

//@Composable
//fun MainScreen() {
//    Column(modifier = Modifier.padding(7.dp)) {
//        Card(
//            elevation = 12.dp,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Row(horizontalArrangement = Arrangement.SpaceAround) {
//                FormatedText("Running")
//                FormatedText("2022/05/12")
//                FormatedText("1h")
//                IconButton(onClick = { /*TODO*/ }, modifier = Modifier
//                    .padding(2.dp)
//                    .padding(start = 50.dp)) {
//                    Icon(imageVector = Icons.Default.PlayArrow,
//                        contentDescription = "", tint = Color.Green)
//                }
//            }
//
//
//        }
//        CountDownTraining(120)
//        var text by remember { mutableStateOf("3467") }
//
//        TextField(
//            value = text,
//            onValueChange = { text = it },
//            label = { Text("Label") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//
//        DropdownDemo(
//            items = listOf("A", "B", "C", "D", "E", "F"),
//        )
//
//    }
//
//}

@Composable
fun FormatedText(text:String) {
    val mod = Modifier.padding(top=16.dp, bottom = 16.dp)
    Text(
        text = text,
        modifier = mod,
        color = Purple200
    )
}

//@Composable
//fun DropdownDemo() {
//    var expanded by remember { mutableStateOf(false) }
//    val items = listOf("A", "B", "C", "D", "E", "F")
//    val disabledValue = "B"
//    var selectedIndex by remember { mutableStateOf(0) }
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.TopStart)) {
//        Text(items[selectedIndex],modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = { expanded = true })
//            .background(
//                Color.Gray
//            ))
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Color.Gray
//                )
//        ) {
//            items.forEachIndexed { index, s ->
//                DropdownMenuItem(onClick = {
//                    selectedIndex = index
//                    expanded = false
//                }) {
//                    val disabledText = if (s == disabledValue) {
//                        " (Disabled)"
//                    } else {
//                        ""
//                    }
//                    Text(text = s + disabledText)
//                }
//            }
//        }
//    }
//}

//                     CountDownTraining(sec = 30, viewModel = viewModel)


@Composable
fun CountDownTraining(sec: Int, viewModel: MainViewModel) {
    val ctx = LocalContext.current
    val text = remember {
        mutableStateOf("Start")
    }
    var counter by remember {
        mutableStateOf(0)
    }

    Spacer(modifier = Modifier.height(50.0.dp))
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()) {
        var col by remember {
            mutableStateOf(Color(0xFF2ABD20))
        }
        viewModel.init(sec)
        FloatingActionButton(
            onClick = {
                counter++
                if (counter % 2 == 1) {
                    text.value = "Stop"
                    viewModel.start(text, ctx)
                    col = Color(0xFFE53935)
                } else {
                    text.value = "Start"
                    viewModel.stop()
                    col = Color(0xFF2ABD20)
                }
            },
            containerColor = col,
            modifier = Modifier.size(150.dp)
        ) {
            Text(text.value, fontSize = 20.sp)
        }
    }

}
