package com.masuta.gogreat.presentation.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ExerciseType
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.presentation.new_training.IconButtonAddExercise
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun WorkoutScreen(
    navController: NavHostController,
    viewModel: WorkoutViewModel
) {
    val listExercises = listOf(
        TrainingExercise(1, "2s", 3,
            12, name = "Squat", relax = "20s", type = "other",uid= ""),
        TrainingExercise(1, "2s", 3,
            12, name = "Deadlift", relax = "20s", type = "other",uid= ""),
        TrainingExercise(1, "2s", 3, 12,
            name = "Bench press",relax = "20s", type = "other",uid= "")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate("main") }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "New training",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                PersonImage()
                Spacer(modifier = Modifier.height(12.dp))
                WorkoutListExercises(listExercises, navController)
            }
        }
    }
}

@Composable
fun WorkoutListExercises(
    listExercises: List<TrainingExercise>,
    navController: NavHostController
) {
    listExercises.forEach { exercise ->
        WorkoutExercise(ex = exercise, onSelectExercise = { navController.navigate("start-training") })
    }
}

@Composable
fun PersonImage() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)) {
        Image(
            painter = painterResource(R.drawable.person),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.5f)
        )
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
//        GlideImage(
//            imageModel = ex.image,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .width(150.dp)
//                .height(100.dp)
//        )
        Image(
            painter = painterResource(id = R.drawable.muscle_dieta),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
        )
        Text(
            text = ex.name,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )
    }
}
