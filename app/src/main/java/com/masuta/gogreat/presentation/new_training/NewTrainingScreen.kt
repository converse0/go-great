package com.masuta.gogreat.presentation.new_training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.masuta.gogreat.Exercise
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.components.BackgroundSurface

@Composable
fun NewTrainingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(bottom = 50.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            ) {
                Text("Save")
            }
        }
        LazyColumn{
            items(1) {
                BackgroundSurface() {
                    PersonSection()
                }
                BackgroundSurface() {
                    ListExercise()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PersonSection() {

    val constraints = ConstraintSet {
        val topGuidLine = createGuidelineFromTop(0.2f)
        val bottomGuidLine = createGuidelineFromBottom(0.4f)
        val person = createRefFor("person")
        val shoulder = createRefFor("shoulder")
        val breast = createRefFor("breast")
        val forearm = createRefFor("forearm")
        val legUp = createRefFor("legUp")
        val legDown = createRefFor("legDown")

        constrain(person) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(shoulder) {
            start.linkTo(person.start, 10.dp)
            top.linkTo(topGuidLine)
        }
        constrain(breast) {
            start.linkTo(person.start)
            end.linkTo(person.end)
            top.linkTo(shoulder.bottom, 10.dp)
        }
        constrain(forearm) {
            start.linkTo(person.start, 5.dp)
            top.linkTo(breast.bottom, 10.dp)
        }
        constrain(legUp) {
            top.linkTo(bottomGuidLine, 10.dp)
            start.linkTo(person.start, 10.dp)
        }
        constrain(legDown) {
            top.linkTo(legUp.bottom, 40.dp)
            start.linkTo(person.start, 10.dp)
        }
    }

    ConstraintLayout(constraintSet = constraints, modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)) {
        Image(
            painter = painterResource(R.drawable.person),
            contentDescription = null,
            modifier = Modifier
                .layoutId("person")
                .fillMaxSize(0.5f)
        )
        IconButtonAddExercise(modifier = Modifier.layoutId("shoulder"))
        IconButtonAddExercise(modifier = Modifier.layoutId("breast"))
        IconButtonAddExercise(modifier = Modifier.layoutId("forearm"))
        IconButtonAddExercise(modifier = Modifier.layoutId("legUp"))
        IconButtonAddExercise(modifier = Modifier.layoutId("legDown"))
    }
}

@Composable
fun IconButtonAddExercise(
    modifier: Modifier = Modifier
) {
    val openWindow = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.LightGray, shape = CircleShape)
            .clip(CircleShape)
            .size(20.dp)
    ) {
        IconButton(
            onClick = { openWindow.value = true }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
        }
    }
    if (openWindow.value) {
        DialogAddExercise(onDismissClick = { openWindow.value = false})
    }
}

@Composable
fun DialogAddExercise(
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = {
            Text(
                text = "Настройка упражнения для выбранной группы мышц",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        text = {
            DialogAddExerciseContent()
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismissClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                ) {
                    Text(text = "Сохранить")
                }
            }
        }
    )
}

@Composable
fun DialogAddExerciseContent() {

    val mod = Modifier
        .padding(bottom = 8.dp)
        .fillMaxWidth()

    var exerciseName by remember { mutableStateOf("") }
    var approach by remember { mutableStateOf("") }
    var countPerApproach by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var restBetweenApproach by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        TextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            placeholder = { Text(text = "Выберите упражнение") },
            modifier = mod,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        TextField(
            value = approach,
            onValueChange = { approach = it },
            placeholder = { Text(text = "Выберите количество подходов") },
            modifier = mod,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = countPerApproach,
            onValueChange = { countPerApproach = it },
            placeholder = { Text(text = "Повторения в каждом подходе") },
            modifier = mod,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = weight,
            onValueChange = { weight = it },
            placeholder = { Text(text = "Вес в каждом повторении") },
            modifier = mod,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = restBetweenApproach,
            onValueChange = { restBetweenApproach = it },
            placeholder = { Text(text = "Отдых между подходами") },
            modifier = mod,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun ListExercise() {
    val exerciseList = listOf(
        Exercise(
            "Отжимание",
            5,
            25,
            60,
            30
        ),
        Exercise(
            "Приседание",
            5,
            25,
            60,
            30
        ),
        Exercise(
            "Подтягивание",
            5,
            25,
            60,
            30
        )

    )

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        exerciseList.forEach { item -> 
            ItemExercise(item = item)
        }
    }
//    LazyColumn(
//        modifier = Modifier.padding(8.dp)
//    ) {
//        items(exerciseList) { item ->
//            ItemExercise(item)
//        }
//    }
}

@Composable
fun ItemExercise(
    item: Exercise
) {
    var addedNewField by remember { mutableStateOf(false) }
    var restTime by remember { mutableStateOf("30") }
    var showInteractionItems by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ){
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = item.name, modifier = Modifier.padding(start = 8.dp))
                if (!addedNewField) {
                    IconButton(onClick = { addedNewField = true }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                } else {
                    IconButton(onClick = { addedNewField = false }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                    }
                }
            }
            if (addedNewField) {
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showInteractionItems) {
                        Text(text = "Введите перерыв: ", modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp))
                        OutlinedTextField(
                            value = restTime,
                            onValueChange = { restTime = it },
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                            modifier = Modifier
                                .width(100.dp)
                                .weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                        IconButton(
                            onClick = { showInteractionItems = false }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green)
                        }
                        IconButton(
                            onClick = { addedNewField = false }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red)
                        }

                    } else {
                        Text(
                            text = "Перерыв $restTime секунд",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        IconButton(onClick = { showInteractionItems = true }) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
