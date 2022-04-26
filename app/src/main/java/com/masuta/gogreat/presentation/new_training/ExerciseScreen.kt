package com.masuta.gogreat.presentation.new_training

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.masuta.gogreat.presentation.components.SliderWithLabel
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.components.SliderWithText
import com.masuta.gogreat.presentation.profile.firstCharToUpperCase
import com.masuta.gogreat.presentation.ui.theme.Red
import com.skydoves.landscapist.glide.GlideImage
import io.ktor.util.reflect.*

@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
    typeId: String?
) {
    val selectedItems = remember { mutableStateOf(listOf(-1)) }
    val newExercise = remember{ mutableStateOf(false) }
    val exercisesList = remember { mutableStateOf(emptyList<TrainingExercise>())  }

    val selectedExerciseId = remember{ mutableStateOf(0) }

    viewModel.getExercises(typeId!!.toLong(), exercisesList)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
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
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val listName =
            if (exercisesList.value.isNotEmpty()) exercisesList.value.first().type.firstCharToUpperCase() + " exercises"
                        else "Empty List exercises"
        Text(
            text = listName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W300
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (exercisesList.value.isNotEmpty()) {
            ExerciseList(
                exercisesList = exercisesList,
                selectedItems = selectedItems.value,
                onNewExercise = {
                    selectedExerciseId.value = it
                    newExercise.value = true
                })
        }
    }
    if (newExercise.value) {
        NewExerciseScreen(
            viewModel = viewModel,
            exercise = exercisesList.value[selectedExerciseId.value],
            onClose = { newExercise.value = false },
            onSubmit = {
                selectedItems.value += selectedExerciseId.value
                newExercise.value = false
                navController.navigate("new-training")
            }
        )
    }
}

@Composable
fun ExerciseList(
    exercisesList: MutableState<List<TrainingExercise>>,
    selectedItems: List<Int>,
    onNewExercise: (Int) -> Unit
) {

    exercisesList.value.forEachIndexed { index, exercise ->
        ExerciseItem(
            exercise = exercise,
            selected = selectedItems.contains(index),
            onClick = {
                onNewExercise(index)
            }
        )
    }
}

@Composable
fun ExerciseItem(
    exercise: TrainingExercise,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = if (selected) Color.Gray else Color.Transparent)
            .height(70.dp)
            .clickable { onClick() }
    ) {
        GlideImage(
            imageModel = exercise.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(70.dp)
        )
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(horizontal = 14.dp)
        )
    }
}

@Composable
fun NewExerciseScreen(
    viewModel: ExerciseViewModel,
    exercise: TrainingExercise,
    onClose: () -> Unit,
    onSubmit: () -> Unit
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
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.W400
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        NewExerciseParameters(exercise = exercise, onSubmit = onSubmit, viewModel = viewModel)
    }
}

fun String.toInteger(): Int = this.filter { it.isDigit() }.toIntOrNull() ?: 30
fun Int.findIndexToFloat(listItems: List<Int>): Float = listItems.indexOf(this).toFloat()

@Composable
fun NewExerciseParameters(
    viewModel: ExerciseViewModel,
    exercise: TrainingExercise,
    onSubmit: () -> Unit
) {

    val counts = listOf(10, 20, 30, 40)
    val sets = listOf(3, 4, 5)
    val repetitions = listOf(15, 30, 50)
    val relax = listOf(5, 20, 30, 50)

    val count = remember { mutableStateOf(exercise.count.findIndexToFloat(counts)) }
//    val duration = remember { mutableStateOf(exercise.duration.toInteger()) }
    val numberOfSets = remember { mutableStateOf(exercise.numberOfSets.findIndexToFloat(sets)) }
    val numberOfRepetitions = remember { mutableStateOf(exercise.numberOfRepetitions.findIndexToFloat(repetitions)) }
    val relaxTime = remember { mutableStateOf(exercise.relax.toInteger().findIndexToFloat(relax)) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = "Count",
                style = MaterialTheme.typography.bodySmall,
            )
//            DropdownDemo(items = listOf(10, 30, 50), selected = count)
            SliderWithLabelUserActivity (
                selectedItem = count,
                valueRange = 0f..counts.size.minus(1).toFloat(),
                items = counts
            )
//            SliderWithLabel(
//                value = 0f,
//                selectedItem = count,
//                valueRange = 0f..counts.size.minus(1).toFloat(),
//                finiteEnd = true,
//                items = counts
//            )
            Text(
                text = "Number of sets",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(10.dp)
            )
//            DropdownDemo(items = listOf(3, 4, 5), selected = numberOfSets)
//            SliderWithLabel(value = 0f, selectedItem = numberOfSets, valueRange = 0f..sets.size.minus(1).toFloat(), finiteEnd = true, items = sets)
            SliderWithLabelUserActivity (
                selectedItem = numberOfSets,
                valueRange = 0f..sets.size.minus(1).toFloat(),
                items = sets
            )
            Text(
                text = "Number of repetitions",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(10.dp)
            )
//            DropdownDemo(items = listOf(15, 30, 50), selected = numberOfRepetitions)
//            SliderWithLabel(value = 0f, selectedItem = numberOfRepetitions, valueRange = 0f..repetitions.size.minus(1).toFloat(), finiteEnd = true, items = repetitions)
            SliderWithLabelUserActivity (
                selectedItem = numberOfRepetitions,
                valueRange = 0f..repetitions.size.minus(1).toFloat(),
                items = repetitions
            )
            Text(
                text = "Choose relax time, sec",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(10.dp)
            )
//            DropdownDemo(items = listOf(20, 30, 50), selected = relaxTime)
//            SliderWithLabel(value = 0f, selectedItem = relaxTime, valueRange = 0f..relax.size.minus(1).toFloat(), finiteEnd = true, items = relax)
            SliderWithLabelUserActivity (
                selectedItem = relaxTime,
                valueRange = 0f..relax.size.minus(1).toFloat(),
                items = relax
            )
//            Text(
//                text = "Relax time, sec",
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(10.dp)
//            )
//
//            DropdownDemo(items = listOf(20, 30, 50), selected = duration)
            TextButton(
                onClick = {
                    onSubmit()
                    val ex = exercise.copy(
                        count = counts.get(count.value.toInt()),
//                        duration = "${duration.value}s",
                        numberOfRepetitions = repetitions.get(numberOfRepetitions.value.toInt()),
                        relax = "${relax.get(relaxTime.value.toInt())}s",
                        numberOfSets = sets.get(numberOfSets.value.toInt())
                    )
                    viewModel.saveLocalExercise(ex)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Red),
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


@OptIn(ExperimentalMaterial3Api::class)
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
            style = MaterialTheme.typography.bodySmall
        )
    }
}