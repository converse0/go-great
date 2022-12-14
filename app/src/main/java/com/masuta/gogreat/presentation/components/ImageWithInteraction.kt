package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import com.masuta.gogreat.core.model.ExerciseType

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
            bottom.linkTo(bottomGuidLine)
            end.linkTo(back.start, 12.dp)
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

        constrain(person) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(arms) {
            end.linkTo(startGuidLine, 30.dp)
            top.linkTo(topGuidLine, 15.dp)
        }
        constrain(legs) {
            bottom.linkTo(bottomGuidLine)
            start.linkTo(person.start, 57.dp)
        }
        constrain(chest) {
            top.linkTo(topGuidLine, 5.dp)
            end.linkTo(arms.start, 20.dp)
        }
        constrain(press) {
            top.linkTo(chest.bottom, 25.dp)
            start.linkTo(chest.start)
        }
        constrain(back) {
            start.linkTo(startGuidLine, 55.dp)
            top.linkTo(press.top)
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
            .background(color = Color.Transparent, shape = CircleShape)
            .clip(CircleShape)
            .size(20.dp)
            .border(width = 2.dp, color = Color.White, shape = CircleShape)
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}