package com.masuta.gogreat.presentation.workout

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.presentation.main.Timer
import com.masuta.gogreat.presentation.main.TimerViewModel
import com.masuta.gogreat.presentation.new_training.findIndexToFloat
import com.masuta.gogreat.presentation.new_training.toInteger
import com.masuta.gogreat.presentation.ui.theme.Green

@Composable
fun StartTrainingScreen(
    navController: NavHostController,
    viewModel: StartTrainingViewModel,
    uid: String?,
) {
    val isEditModal = remember { mutableStateOf(false) }
    val isModalOpen = remember { mutableStateOf(false) }
    val isDurationTimer = remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (viewModel.listExercises.value.isEmpty()) {
        viewModel.getTraining(uid!!)
    }

    val listExercises = viewModel.listExercises
    val indexExercise = viewModel.indexExercise
    val exerciseSets = viewModel.exerciseSets
    val currentExercise = viewModel.currentExercise.value

    val timerViewModel: TimerViewModel = hiltViewModel()

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
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            }
            Text(
                text = currentExercise.name,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                VideoSection(url = currentExercise.video)
                Spacer(modifier = Modifier.height(12.dp))
                TrainingInfo(
                    currentExercise,
                    exerciseSets = exerciseSets.value,
                    onOpenModal = {
                        viewModel.nextSetOrTraining(
                            onOpenModal = { isModalOpen.value = true }
                        )
                    },
                    onOpenEdit = {
                        isEditModal.value = true
                    },
                    onDurationTimer = {
                        isDurationTimer.value = true
                    }
                )
            }
        }
    }
    if (isModalOpen.value) {
        ModalTimer(
            totalTime = currentExercise.relax.toInteger().toLong(),
            viewModel = viewModel,
            onDismiss = {
                isModalOpen.value = false
                timerViewModel.stopOnClose()
                if (viewModel.indexExercise.value >= viewModel.listExercises.value.size) {
                    viewModel.endTraining(navController,context)
                    viewModel.finishTraining(uid!!)
                }
            },
        )
    }

    if (isDurationTimer.value) {
        ModalTimer(
            totalTime = currentExercise.duration.toInteger().toLong(),
            viewModel = viewModel,
            onDismiss = {
                isDurationTimer.value = false
                timerViewModel.stopOnClose()
            }
        )
    }

    if (isEditModal.value) {
        val weight = remember { mutableStateOf("") }
        val time = remember { mutableStateOf(currentExercise.relax.toInteger()) }
        val numberOfSets = remember {
            mutableStateOf(currentExercise.numberOfSets.toString())
        }
        val numberOfRepetitions =
            remember { mutableStateOf(currentExercise.numberOfRepetitions.toString()) }

        val duration = viewModel.listDuration

        val durationTime = remember {
            mutableStateOf(currentExercise.duration.toInteger().findIndexToFloat(duration))
        }

        StartTrainingModal(
            viewModel,
            weight,
            time,
            durationTime,
            numberOfSets,
            numberOfRepetitions,
            onSave = {
                val listEditExercise = listExercises.value.mapIndexed { index, exercise ->
                    if (index == indexExercise.value) {
                        exercise.copy(
                            relax = time.value.toString() + "s",
                            numberOfSets = numberOfSets.value.toInt(),
                            numberOfRepetitions = numberOfRepetitions.value.toInt(),
                            duration = "${duration.get(durationTime.value.toInt())}s",
                        )
                    } else {
                        exercise
                    }
                }
                viewModel.setExerciseParams(
                    uid = uid!!,
                    listExercises = listEditExercise
                )
                isEditModal.value = false
            },
            onDismiss = { isEditModal.value = false }
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModalTimer(
    totalTime: Long,
    viewModel: StartTrainingViewModel,
    onDismiss: () -> Unit,
) {

    val context = LocalContext.current

    val sound = {
        viewModel.playSound(context)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
            .background(color = Color.Black)
            .clickable { }
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
        Card(
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Timer(
                    totalTime = totalTime * 1000L,
                    onAlarmSound = sound,
                    onTimerEnd = onDismiss,
                    startTimer = true,
                    modifier = Modifier.size(200.dp),
                )
            }
        }
    }
}

@Composable
fun TrainingInfo(
    exercise: TrainingExercise,
    exerciseSets: Int,
    onOpenModal: () -> Unit,
    onOpenEdit: () -> Unit,
    onDurationTimer: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Number of repetitions",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = exercise.numberOfRepetitions.toString(),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Number of sets",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${exerciseSets} / ${exercise.numberOfSets}",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    ButtonSection(
        exercise = exercise,
        onOpenModal = onOpenModal,
        onOpenEdit = onOpenEdit,
        onDurationTimer = onDurationTimer
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
    DescriptionSection(description = exercise.description)

    Spacer(Modifier.height(10.dp))

    Text(
        text = "Technique",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(10.dp))

    TechniqueSection(technique = exercise.technique)

    Spacer(Modifier.height(10.dp))

    Text(
        text = "Common mistakes",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    MistakesSection(mistake = exercise.mistake)

}

@Composable
fun MistakesSection(
    mistake: String
) {
    Text(
        text = mistake,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TechniqueSection(
    technique: String
) {
    Text(
        text = technique,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun DescriptionSection(
    description: String
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun ButtonSection(
    exercise: TrainingExercise,
    onOpenModal: () -> Unit,
    onOpenEdit: () -> Unit,
    onDurationTimer: () -> Unit
) {

    val isEnabled = remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = {
                onOpenModal()
                isEnabled.value = true
            },
            modifier = Modifier
                .width(100.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "I managed!", color = Color.White)
        }

        if (exercise.type == "other") {
            TextButton(
                enabled = isEnabled.value,
                onClick = {
                    onDurationTimer()
                    isEnabled.value = false
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Go", color = Color.White)
            }
        }

        TextButton(
            onClick = {
                onOpenEdit()
                isEnabled.value = true
            },
            modifier = Modifier
                .width(100.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, color = Color.Gray),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Edit!", color = Color.Black)
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun VideoSection(
    url: String
) {
    val videoUrl = url.ifEmpty { "https://cdn.videvo.net/videvo_files/video/free/2018-09/large_watermarked/180419_Boxing_06_01_preview.mp4" }
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSource = DefaultDataSource.Factory(context)
            val source = ProgressiveMediaSource.Factory(dataSource)
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

            addMediaSource(source)
            prepare()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = true
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        )
    }
}