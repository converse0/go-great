package com.masuta.gogreat.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.gender
import com.masuta.gogreat.presentation.components.FemalePersonSectionWithPoint
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.components.MalePersonSectionWithPoint
import com.masuta.gogreat.presentation.ui.theme.Red
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun WorkoutScreen(
    navController: NavHostController,
    viewModel: WorkoutViewModel,
    uid: String?
) {

    val listExercises = remember { mutableStateOf(emptyList<TrainingExercise>()) }
    val name = remember { mutableStateOf("") }
    
    viewModel.getExercises(uid!!, listExercises, name)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    navController.navigate("main")
                }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                }
                Text(
                    text = name.value,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    gender?.let { n ->
                        if (n == 0) MalePersonSectionWithPoint(listPoints = listExercises.value)
                        else FemalePersonSectionWithPoint(listPoints = listExercises.value)

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    WorkoutListExercises(listExercises.value)
                    Spacer(Modifier.height(30.dp))
                }
            }
        }
        MainTextButton(
            text = "Start Training",
            color = Red,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            viewModel.startTraining(uid)
            navController.navigate("start-training/$uid")
        }
    }
}

@Composable
fun WorkoutListExercises(
    listExercises: List<TrainingExercise>,
) {
    listExercises.forEach { exercise ->
        WorkoutExercise(ex = exercise, onSelectExercise = {  })
    }
}

@Composable
fun WorkoutExercise(
    ex: TrainingExercise,
    onSelectExercise: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelectExercise() },
    ) {
        GlideImage(
            imageModel = ex.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(70.dp)
        )
        Text(
            text = ex.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

