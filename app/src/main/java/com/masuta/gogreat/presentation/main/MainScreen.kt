package com.masuta.gogreat.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.ui.theme.Purple200
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.*

@Composable
fun MainScreen(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>,
    viewModel: MainViewModel
) {
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
        ) {
            Text(
                text = "Workouts",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.W400
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Text(
                        text = "Create your workout today according to your personal preferences",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    TextButton(
                        onClick = {
                            navController.navigate("new-training")
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Create new training",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    WorkoutsSection(viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}

@Composable
fun WorkoutsSection(
    viewModel: MainViewModel,
    navController: NavHostController
) {
//    val listTrainings = listOf(
//        Training(
//            listOf(TrainingExercise(1, "2", 3,
//                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3,
//                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3, 12,
//                    name = "Bench press",relax = "20s", type = "weight",uid= "")
//            ),
//            "20",
//            "Dumbbell lifting"
//        ),
//        Training(
//            listOf(TrainingExercise(1, "2", 3,
//                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3,
//                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3, 12,
//                    name = "Bench press",relax = "20s", type = "weight",uid= "")
//            ),
//            "20",
//            "Dumbbell lifting"
//        ),
//        Training(
//            listOf(TrainingExercise(1, "2", 3,
//                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3,
//                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
//                TrainingExercise(1, "2", 3, 12,
//                    name = "Bench press",relax = "20s", type = "weight",uid= "")
//            ),
//            "20",
//            "Dumbbell lifting"
//        ),
//    )

    val listTrainings = remember { mutableStateOf(emptyList<Training>()) }

    viewModel.getExercises(listTrainings)

    if (listTrainings.value.isNotEmpty()) {
        Text(
            text = "My workouts",
            style = MaterialTheme.typography.h5,
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
    LazyRow() {
        items(workouts) { workout ->
            WorkoutItem(workout = workout, onSelectItem = { navController.navigate("workout/${it}") })
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Training,
    onSelectItem: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 20.dp)
            .clickable { onSelectItem(workout.uid!!) }
    ) {
        workout.image?.let {
            println("image: $it")
            GlideImage(imageModel = it,
                modifier = Modifier.size(200.dp),
                colorFilter = ColorFilter.lighting(multiply = Color.Gray,add= Color.Black)
            )

        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W700
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "27 March 2017 ${workout.interval} min",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W300
            )
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
                    viewModel.start(text)
                    col = Color(0xFFE53935)
                } else {
                    text.value = "Start"
                    viewModel.stop()
                    col = Color(0xFF2ABD20)
                }
            },
            backgroundColor = col,
            modifier = Modifier.size(150.dp)
        ) {
            Text(text.value, fontSize = 20.sp)
        }
    }

}
