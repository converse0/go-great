package com.masuta.gogreat.presentation.new_training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ExerciseType
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.presentation.ui.theme.SportTheme


@Composable
fun NewTrainingScreen(
    navController: NavHostController,
    viewModel: NewTrainingViewModel
) {

    val openModal = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val listExercises = remember { mutableStateOf(emptyList<TrainingExercise>()) }
  
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
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
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                PersonSection(onNewExercise = { navController.navigate("list-exercise") })
                Text(
                    text = "Please, press + to choose a group of muscles and add exercise",
                    style = MaterialTheme.typography.body1
                )
                Spacer(Modifier.height(20.dp))
                ExercisesList(listExercises)
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = {
                        openModal.value = true
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    if (openModal.value) {
        Modal(
            onSave = {
                viewModel.saveTrain(
                    newTrain = Training(
                        exercises = listExercises.value,
                        interval = "50",
                        name = it
                    )
                )
            }
        )
    }
}

@Composable
fun Modal(
    onSave: (String) -> Unit
) {
    val name = remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
            .background(color = Color.Black)
        )
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    text = "Please, name this workout",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(30.dp))
                TextButton(
                    onClick = {
                        onSave(name.value)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Create new training",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExercisesList(
    listExercises: MutableState<List<TrainingExercise>>
) {
    listExercises.value.forEach { exercise ->
        ExercisesItem(title = exercise.name)
    }
}

@Composable
fun ExercisesItem(
    image: String = "",
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(painter = painterResource(id = R.drawable.muscle_dieta), contentDescription = null)
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PersonSection(
    onNewExercise: () -> Unit
) {
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
        ExerciseType.values().forEach { type ->
            val layoutId = when(type) {
                ExerciseType.ARMS -> "shoulder"
                ExerciseType.LEGS -> "legDown"
                ExerciseType.OTHER -> "forearm"
            }
            IconButtonAddExercise(modifier = Modifier.layoutId(layoutId), onClick = onNewExercise)
        }

//        IconButtonAddExercise(modifier = Modifier.layoutId("shoulder"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("breast"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("forearm"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("legUp"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("legDown"), onClick = onNewExercise)
    }
}

@Composable
fun IconButtonAddExercise(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color.LightGray, shape = CircleShape)
            .clip(CircleShape)
            .size(20.dp)
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
        }
    }
}

@Preview
@Composable
fun NewTrainingScreenPreview() {
    SportTheme() {
        NewTrainingScreen(
            navController = NavHostController(LocalContext.current),
            viewModel = hiltViewModel()
        )
    }

}


