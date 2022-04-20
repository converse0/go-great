package com.masuta.gogreat.presentation.main

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.presentation.workout.PersonImage


@Composable
fun WorkoutListExercises2(
    listExercises: List<TrainingExercise>,
    navController: NavHostController? = null,
    uid: String?,
    numberOfExercise: MutableState<Int>,
    sets: MutableState<Int>,
) {
    if (numberOfExercise.value == listExercises.size) {
        navController?.navigate(
           "main"
        )
        return
    }
    val ex = listExercises.get(numberOfExercise.value)
    val set = ex.numberOfSets
    sets.value = set
    val name = ex.name
    Column() {
        Text(text = "Exercise: length: ${listExercises.size}")
        Row {
            Text(text = name)

            Spacer(modifier = Modifier.width(20.dp))
            Text(text = set.toString())
        }

    }

}


@Composable
fun TrainingList(viewModel: MainViewModel) {
    val exercises = remember { mutableStateOf(listOf<Training>()) }
    val sets = remember { mutableStateOf(0) }
    val numberOfExercise = remember { mutableStateOf(0) }
//    viewModel.getExercises(exercises, countTotalWorkout)
    if (exercises.value.isEmpty()) {
        Text("No exercises")
        return
    }
    val ex = exercises.value.last().exercises
    val cxt = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            PersonImage()
            Spacer(modifier = Modifier.height(12.dp))
            WorkoutListExercises2(ex, null, null, numberOfExercise = numberOfExercise, sets)
            Spacer(Modifier.height(30.dp))
            TextButton(
                onClick = {
//                    viewModel.startTraining(uid)
//                    navController.navigate("start-training/$uid")
                    sets.value--
                    if (sets.value == 0) {
                        numberOfExercise.value++
                    }

                        Toast.makeText(cxt, "test $sets", Toast.LENGTH_SHORT).show()

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Start training",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}