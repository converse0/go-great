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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import com.masuta.gogreat.Exercise
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BackgroundSurface
import com.masuta.gogreat.presentation.profile.ProfileSection
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun NewTrainingScreen(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
) {
    var newExercise by remember { mutableStateOf(false) }

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
            PersonSection(onNewExerciseScreen = { newExercise = true })
        }
        if (newExercise) {
            NewExerciseScreen(onClose = { newExercise = false })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PersonSection(
    onNewExerciseScreen: () -> Unit
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
        IconButtonAddExercise(modifier = Modifier.layoutId("shoulder"), onClick = onNewExerciseScreen)
        IconButtonAddExercise(modifier = Modifier.layoutId("breast"), onClick = onNewExerciseScreen)
        IconButtonAddExercise(modifier = Modifier.layoutId("forearm"), onClick = onNewExerciseScreen)
        IconButtonAddExercise(modifier = Modifier.layoutId("legUp"), onClick = onNewExerciseScreen)
        IconButtonAddExercise(modifier = Modifier.layoutId("legDown"), onClick = onNewExerciseScreen)
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

@Composable
fun NewExerciseScreen(
    onClose: () -> Unit
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
