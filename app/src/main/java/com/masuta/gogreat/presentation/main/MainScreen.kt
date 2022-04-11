package com.masuta.gogreat.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.ui.theme.Purple200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
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
                    menuItems.forEach { item ->
                        IconButton(onClick = {
                            navController.navigate(item.route)
                            onSelect(item.route)
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
                    WorkoutsSection()
                }
            }
        }
    }
}

@Composable
fun WorkoutsSection() {
    val listTrainings = listOf(
        Training(
            listOf(TrainingExercise(1, "2", 3,
                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3,
                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3, 12,
                    name = "Bench press",relax = "20s", type = "weight",uid= "")
            ),
            "20",
            "Dumbbell lifting"
        ),
        Training(
            listOf(TrainingExercise(1, "2", 3,
                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3,
                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3, 12,
                    name = "Bench press",relax = "20s", type = "weight",uid= "")
            ),
            "20",
            "Dumbbell lifting"
        ),
        Training(
            listOf(TrainingExercise(1, "2", 3,
                12, name = "Squat", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3,
                    12, name = "Deadlift", relax = "20s", type = "weight",uid= ""),
                TrainingExercise(1, "2", 3, 12,
                    name = "Bench press",relax = "20s", type = "weight",uid= "")
            ),
            "20",
            "Dumbbell lifting"
        ),
    )

    if (listTrainings.isNotEmpty()) {
        Text(
            text = "My workouts",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.W300,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )
        WorkoutsList(workouts = listTrainings)
    }
}

@Composable
fun WorkoutsList(
    workouts: List<Training>
) {
    LazyRow() {
        items(workouts) { workout ->
            WorkoutItem(workout = workout)
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Training
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 20.dp)
    ) {
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

@Composable
fun CountDownTraining(sec: Int) {
    var text by remember {
        mutableStateOf("Start")
    }
    Spacer(modifier = Modifier.height(50.0.dp))
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()) {
        val col by remember {
            mutableStateOf(Color(0xFF2ABD20))
        }
        FloatingActionButton(
            //col = Color(0xFF0E4E09)
            onClick = {
                val job = CoroutineScope(Dispatchers.Main).launch {
                    val seq = 0..sec
                    for (i in seq.reversed()) {
                        delay(1000)
                        if(i % 10 == 0) println("ping $i")
                        text = i.toString()
                    }
                }
            },
            backgroundColor = col,
            modifier = Modifier.size(150.dp)
        ) {
            Text(text, fontSize = 20.sp)
        }
    }

}
