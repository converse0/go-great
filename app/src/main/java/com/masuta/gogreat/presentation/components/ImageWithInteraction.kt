package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ExerciseType

//enum class ExerciseTypeLocal(val value: Int) {
//    ARMS(0),
//    LEGS(1),
//    PRESS(2),
//    BACK(3),
//    CHEST(4),
////    SHOULDER(5),
////    TRICEPS(6),
////    BICEPS(7),
//    OTHER(8)
//}

@Composable
fun FemalePersonSection(
    onNewExercise: (ExerciseType) -> Unit
) {
    val constraints = ConstraintSet {
        val topGuidLine = createGuidelineFromTop(0.2f)
        val bottomGuidLine = createGuidelineFromBottom(0.4f)
        val startGuidLine = createGuidelineFromStart(0.5f)
        val person = createRefFor("person")
        val arms = createRefFor("arms")
        val legs = createRefFor("legs")
        val press = createRefFor("press")
        val chest = createRefFor("chest")
        val back = createRefFor("back")
        val shoulder = createRefFor("shoulder")
        val triceps = createRefFor("triceps")
        val biceps = createRefFor("biceps")
        val other = createRefFor("other")

        constrain(person) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(arms) {
            end.linkTo(startGuidLine, 20.dp)
            top.linkTo(topGuidLine)
        }
        constrain(legs) {
            top.linkTo(bottomGuidLine, 40.dp)
            start.linkTo(person.start, 45.dp)
        }
        constrain(chest) {
            top.linkTo(topGuidLine, 5.dp)
            start.linkTo(startGuidLine, 75.dp)
        }
        constrain(press) {
            top.linkTo(chest.bottom, 25.dp)
            start.linkTo(chest.start)
        }
        constrain(back) {
            end.linkTo(arms.start, 5.dp)
            top.linkTo(press.top)
        }
        constrain(biceps) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(triceps) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(shoulder) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(other) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
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
                painter = painterResource(R.drawable.human_femail),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("person")
                    .height(350.dp)
                    .width(350.dp)
            )
            ExerciseType.values().forEach { type ->
                val layoutId = when(type) {
                    ExerciseType.ARMS -> "arms"
                    ExerciseType.LEGS -> "legs"
                    ExerciseType.PRESS -> "press"
                    ExerciseType.BACK -> "back"
                    ExerciseType.CHEST -> "chest"
                    ExerciseType.SHOULDER -> "shoulder"
                    ExerciseType.TRICEPS -> "triceps"
                    ExerciseType.BICEPS -> "biceps"
                    ExerciseType.OTHER -> "other"
                }
                IconButtonAddExercise(modifier = Modifier.layoutId(layoutId), onClick = { onNewExercise(type) })
            }
        }
    }
}

@Composable
fun MalePersonSection(
    onNewExercise: (ExerciseType) -> Unit
) {
    val constraints = ConstraintSet {
        val topGuidLine = createGuidelineFromTop(0.2f)
        val bottomGuidLine = createGuidelineFromBottom(0.4f)
        val startGuidLine = createGuidelineFromStart(0.5f)
        val person = createRefFor("person")
        val arms = createRefFor("arms")
        val legs = createRefFor("legs")
        val press = createRefFor("press")
        val chest = createRefFor("chest")
        val back = createRefFor("back")
        val shoulder = createRefFor("shoulder")
        val triceps = createRefFor("triceps")
        val biceps = createRefFor("biceps")
        val other = createRefFor("other")

        constrain(person) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(arms) {
            end.linkTo(startGuidLine, 25.dp)
            top.linkTo(topGuidLine, 15.dp)
        }
        constrain(legs) {
            top.linkTo(bottomGuidLine, 40.dp)
            start.linkTo(person.start, 55.dp)
        }
        constrain(chest) {
            top.linkTo(topGuidLine, 5.dp)
            end.linkTo(arms.start, 25.dp)
        }
        constrain(press) {
            top.linkTo(chest.bottom, 25.dp)
            start.linkTo(chest.start)
        }
        constrain(back) {
            start.linkTo(startGuidLine, 55.dp)
            top.linkTo(press.top)
        }
        constrain(biceps) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(triceps) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(shoulder) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
        }
        constrain(other) {
            start.linkTo(startGuidLine)
            bottom.linkTo(bottomGuidLine)
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
                painter = painterResource(R.drawable.human),
                contentDescription = null,
                modifier = Modifier
                    .layoutId("person")
                    .height(350.dp)
                    .width(350.dp)
            )
            ExerciseType.values().forEach { type ->
                val layoutId = when(type) {
                    ExerciseType.ARMS -> "arms"
                    ExerciseType.LEGS -> "legs"
                    ExerciseType.PRESS -> "press"
                    ExerciseType.BACK -> "back"
                    ExerciseType.CHEST -> "chest"
                    ExerciseType.SHOULDER -> "shoulder"
                    ExerciseType.TRICEPS -> "triceps"
                    ExerciseType.BICEPS -> "biceps"
                    ExerciseType.OTHER -> "other"
                }
                IconButtonAddExercise(modifier = Modifier.layoutId(layoutId), onClick = { onNewExercise(type) })
            }
        }
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
            .background(color = Color.Yellow, shape = CircleShape)
            .clip(CircleShape)
            .size(20.dp)
//            .border(width = 2.dp, color = Color.White, shape = CircleShape)
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }
}