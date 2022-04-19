package com.masuta.gogreat.presentation.diet

import android.app.Person
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ExerciseType
import com.masuta.gogreat.domain.model.gender
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.auth.SignInForm
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.new_training.IconButtonAddExercise
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun DietScreen(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
) {
    Scaffold(
        bottomBar = {
            BottomMenuBar(navController = navController, selected = selected, onSelect = onSelect, menuItems = menuItems)
        }
    ) {

//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//                .background(color = Color.White)
//                .padding(20.dp)
//        ) {
//            Text(
//                text = "My diet",
//                style = MaterialTheme.typography.h3,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//            Spacer(modifier = Modifier.height(40.dp))
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(16.dp))
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.muscle_dieta),
//                    contentDescription = "Image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(250.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                )
//            }
//            Spacer(modifier = Modifier.height(40.dp))
//            Text(
//                text = "Coming soon!",
//                style = MaterialTheme.typography.h5,
//                fontWeight = FontWeight.W700
//            )
//        }
        PersonSection(onNewExercise = {})
    }
}

// Testing 

@Composable
fun PersonSection(
    onNewExercise: (ExerciseType) -> Unit
) {
    val constraints = ConstraintSet {
        val topGuidLine = createGuidelineFromTop(0.2f)
        val bottomGuidLine = createGuidelineFromBottom(0.4f)
        val startGuidLine = createGuidelineFromStart(0.5f)
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
            end.linkTo(startGuidLine, 20.dp)
//            start.linkTo(person.start, 80.dp)
            top.linkTo(topGuidLine)
        }
        constrain(breast) {
            start.linkTo(person.start)
            end.linkTo(person.end)
            top.linkTo(shoulder.bottom, 10.dp)
        }
        constrain(forearm) {
            start.linkTo(person.start, 80.dp)
            top.linkTo(breast.bottom, 10.dp)
        }
        constrain(legUp) {
            top.linkTo(bottomGuidLine, 10.dp)
            start.linkTo(person.start, 10.dp)
        }
        constrain(legDown) {
            top.linkTo(legUp.bottom, 40.dp)
            start.linkTo(person.start, 40.dp)
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout(
            constraintSet = constraints,
            modifier = Modifier
                .height(350.dp)
                .width(350.dp)
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(
                    gender?.let {
                        if (it == 0) R.drawable.human else R.drawable.human_femail
                    } ?: R.drawable.human
                ),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("person")
                    .height(350.dp)
                    .width(350.dp)
            )
            ExerciseType.values().forEach { type ->
                val layoutId = when(type) {
                    ExerciseType.ARMS -> "shoulder"
                    ExerciseType.LEGS -> "legDown"
                    ExerciseType.OTHER -> "forearm"
                }
                IconButtonAddExercise(modifier = Modifier.layoutId(layoutId), onClick = { onNewExercise(type) })
            }

//        IconButtonAddExercise(modifier = Modifier.layoutId("shoulder"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("breast"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("forearm"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("legUp"), onClick = onNewExercise)
//        IconButtonAddExercise(modifier = Modifier.layoutId("legDown"), onClick = onNewExercise)
        }
    }
}

@Preview
@Composable
fun PersonSectionPreview() {
    SportTheme() {
        PersonSection(onNewExercise = {})
    }
}
