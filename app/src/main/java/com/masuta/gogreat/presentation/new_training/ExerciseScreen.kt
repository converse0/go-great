package com.masuta.gogreat.presentation.new_training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.TrainingExercise

@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
    id: Long
) {
    val exercisesList = remember { mutableStateOf(emptyList<TrainingExercise>()) }

    viewModel.getExercises(id, exercisesList)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate("new-training") }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "List of exercise",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Biceps exercise",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.W300
        )
        if (exercisesList.value.isNotEmpty()) {
            ExerciseList(exercisesList)
        }
    }
}

@Composable
fun ExerciseList(
    exercisesList: MutableState<List<TrainingExercise>>,
) {

    val newExercise = remember{ mutableStateOf(false) }

    exercisesList.value.forEachIndexed { index, exercise ->
        ExerciseItem(
            id = index,
            title = exercise.name,
            selected = false,
            onClick = {
                newExercise.value = true
            }
        )
    }

    if (newExercise.value) {
        NewExerciseScreen(
            onClose = { newExercise.value = false }
        )
    }

}

@Composable
fun ExerciseItem(
//    image: Int,
    id: Int,
    title: String,
    selected: Boolean,
    onClick: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (selected) Color.Gray else Color.Transparent)
            .clip(RoundedCornerShape(16.dp))
            .height(100.dp)
            .clickable { onClick(id) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.sport_health),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
fun NewExerciseScreen(
    onClose: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "New Exercise",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.W400
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        NewExerciseParameters()
    }
}

@Composable
fun NewExerciseParameters() {
    val sessions = remember { mutableStateOf(3) }
    val repetitions = remember { mutableStateOf(14) }
    val weight = remember { mutableStateOf(20) }
    val recoveryTime = remember { mutableStateOf(30) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = "Number of session",
                style = MaterialTheme.typography.body1,
            )
            NumberSession(selected = sessions.value, onSessionSelect = { sessions.value = it })
            Text(
                text = "Number of repetitions",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(10.dp)
            )
            NumberRepetitions(selected = repetitions.value, onRepetitionSelect = { repetitions.value = it })
            Text(
                text = "Chose weight, kg",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(10.dp)
            )
            NumberWeight(selected = weight.value, onWeightSelect = { weight.value = it })
            Text(
                text = "Choose recovery time, sec",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(10.dp)
            )
            NumberRecoveryTime(selected = recoveryTime.value, onTimeSelect = { recoveryTime.value = it })
            TextButton(
                onClick = {
                    /*TODO*/
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {
                Text(text = "Submit", color = Color.White, modifier = Modifier.padding(vertical = 16.dp))
            }
        }

    }
}

@Composable
fun NumberSession(
    selected: Int,
    onSessionSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "3", selected = selected == 3, onSelect = { onSessionSelect(3) })
        DefaultRadioButton(text = "4", selected = selected == 4, onSelect = { onSessionSelect(4) })
    }
}

@Composable
fun NumberRepetitions(
    selected: Int,
    onRepetitionSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "14", selected = selected == 14, onSelect = { onRepetitionSelect(14) })
        DefaultRadioButton(text = "15", selected = selected == 15, onSelect = { onRepetitionSelect(15) })
    }
}

@Composable
fun NumberWeight(
    selected: Int,
    onWeightSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "20", selected = selected == 20, onSelect = { onWeightSelect(20) })
        DefaultRadioButton(text = "25", selected = selected == 25, onSelect = { onWeightSelect(25) })
    }
}

@Composable
fun NumberRecoveryTime(
    selected: Int,
    onTimeSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "30", selected = selected == 30, onSelect = { onTimeSelect(30) })
        DefaultRadioButton(text = "60", selected = selected == 60, onSelect = { onTimeSelect(60) })
    }
}


@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.body1
        )
    }
}