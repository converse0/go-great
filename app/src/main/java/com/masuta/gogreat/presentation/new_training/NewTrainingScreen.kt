package com.masuta.gogreat.presentation.new_training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ExerciseType
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.gender
import com.masuta.gogreat.presentation.components.FemalePersonSection
import com.masuta.gogreat.presentation.components.MalePersonSection
import com.masuta.gogreat.presentation.ui.theme.Green
import com.masuta.gogreat.presentation.ui.theme.Red
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import com.skydoves.landscapist.glide.GlideImage

private fun replaceString(str: String) = str.replace(" ", "_")

@Composable
fun NewTrainingScreen(
    navController: NavHostController,
    viewModel: NewTrainingViewModel
) {

    val openModal = remember { mutableStateOf(false) }
    val listExercises = remember { mutableStateOf(listOf<TrainingExercise>()) }

    viewModel.getLocalExercises(listExercises)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.navigate("main")
                viewModel.clearLocalExercises()
            }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "New training",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
            ) {
                item {
//                    PersonSection(onNewExercise = { navController.navigate("list-exercise/${it.value}") })
                    gender?.let { n ->
                        if (n == 0) MalePersonSection(onNewExercise = { navController.navigate("list-exercise/${it.value}") }) else
                            FemalePersonSection(onNewExercise = { navController.navigate("list-exercise/${it.value}") })
                    }
                    Text(
                        text = "Please, press + to choose a group of muscles and add exercise",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    ExercisesList(listExercises)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            TextButton(
                onClick = {
                    openModal.value = true
                },
                enabled= listExercises.value.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Save",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
    if (openModal.value) {
        Modal(
            onSave = {
                viewModel.saveTrain(
                    newTrain = Training(
                        exercises = listExercises.value,
                        interval = "50s",
                        name = it,
//                        image = "https://fitseven.ru/wp-content/uploads/2020/07/uprazhneniya-na-press-skruchivaniya.jpg"
                    )
                )
                viewModel.clearLocalExercises()
                navController.navigate("main")
            },
            onDismiss = { openModal.value = false }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Modal(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
            .background(color = Color.Black)
            .clickable { onDismiss() }
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    )
                )
                Spacer(Modifier.height(30.dp))
                TextButton(
                    onClick = {
                        onSave(name.value)
                    },
                    enabled = name.value.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
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
        ExercisesItem(exercise)
    }
}

@Composable
fun ExercisesItem(
    ex: TrainingExercise,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        GlideImage(
            imageModel = ex.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
        )
//        Image(
//            painter = painterResource(id = R.drawable.muscle_dieta),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .width(200.dp)
//                .height(100.dp)
//        )
        Text(
            text = ex.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(horizontal = 15.dp)
//                .clickable {
//                    //TODO
//
//                }
        )
    }
}

//@Composable
//fun PersonSection(
//    onNewExercise: (ExerciseType) -> Unit
//) {
//    val constraints = ConstraintSet {
//        val topGuidLine = createGuidelineFromTop(0.2f)
//        val bottomGuidLine = createGuidelineFromBottom(0.4f)
//        val person = createRefFor("person")
//        val shoulder = createRefFor("shoulder")
//        val breast = createRefFor("breast")
//        val forearm = createRefFor("forearm")
//        val legUp = createRefFor("legUp")
//        val legDown = createRefFor("legDown")
//
//        constrain(person) {
//            top.linkTo(parent.top)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        }
//        constrain(shoulder) {
//            start.linkTo(person.start, 80.dp)
//            top.linkTo(topGuidLine)
//        }
//        constrain(breast) {
//            start.linkTo(person.start)
//            end.linkTo(person.end)
//            top.linkTo(shoulder.bottom, 10.dp)
//        }
//        constrain(forearm) {
//            start.linkTo(person.start, 80.dp)
//            top.linkTo(breast.bottom, 10.dp)
//        }
//        constrain(legUp) {
//            top.linkTo(bottomGuidLine, 10.dp)
//            start.linkTo(person.start, 10.dp)
//        }
//        constrain(legDown) {
//            top.linkTo(legUp.bottom, 40.dp)
//            start.linkTo(person.start, 80.dp)
//        }
//    }
//
//    ConstraintLayout(constraintSet = constraints, modifier = Modifier
//        .fillMaxWidth()
//        .padding(12.dp)) {
//        Image(
//            painter = painterResource(
//                gender?.let {
//                    if (it == 0) R.drawable.human else R.drawable.human_femail
//                } ?: R.drawable.human
//            ),
//            contentDescription = null,
//            modifier = Modifier
//                .layoutId("person")
//                .height(300.dp)
//                .width(300.dp)
//        )
//        ExerciseType.values().forEach { type ->
//            val layoutId = when(type) {
//                ExerciseType.ARMS -> "shoulder"
//                ExerciseType.LEGS -> "legDown"
//                ExerciseType.OTHER -> "forearm"
//            }
//            IconButtonAddExercise(modifier = Modifier.layoutId(layoutId), onClick = { onNewExercise(type) })
//        }
//
////        IconButtonAddExercise(modifier = Modifier.layoutId("shoulder"), onClick = onNewExercise)
////        IconButtonAddExercise(modifier = Modifier.layoutId("breast"), onClick = onNewExercise)
////        IconButtonAddExercise(modifier = Modifier.layoutId("forearm"), onClick = onNewExercise)
////        IconButtonAddExercise(modifier = Modifier.layoutId("legUp"), onClick = onNewExercise)
////        IconButtonAddExercise(modifier = Modifier.layoutId("legDown"), onClick = onNewExercise)
//    }
//}

//@Composable
//fun IconButtonAddExercise(
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//            .background(color = Color.Yellow, shape = CircleShape)
//            .clip(CircleShape)
//            .size(20.dp)
////            .border(width = 2.dp, color = Color.White, shape = CircleShape)
//    ) {
//        IconButton(
//            onClick = onClick
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = null,
//                tint = Color.Black,
//            )
//        }
//    }
//}


