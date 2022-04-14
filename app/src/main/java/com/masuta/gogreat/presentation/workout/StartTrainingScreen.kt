package com.masuta.gogreat.presentation.workout

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.presentation.main.Timer
import com.masuta.gogreat.presentation.new_training.toInteger
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun StartTrainingScreen(
    navController: NavHostController,
    viewModel: StartTrainingViewModel,
    uid: String?,
    exerciseId: String?
) {

    val isModalOpen = remember { mutableStateOf(false) }
    val exercise = viewModel.getExerciseById(exerciseId!!)

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
            IconButton(onClick = { navController.navigate("workout/${uid}") }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                VideoSection(url = exercise.video)
                Spacer(modifier = Modifier.height(12.dp))
                TrainingInfo(
                    exercise,
                    onOpenModal = {
                        viewModel.startTraining(uid!!)
                        isModalOpen.value = true
                    }
                )
            }
        }
    }
    if (isModalOpen.value) {
        ModalTimer(exercise.duration.toInteger().toLong(), onDismiss = { isModalOpen.value = false })
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModalTimer(
    totalTime: Long,
    onDismiss: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
            .background(color = Color.Black)
            .clickable {  }
        )
        IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Timer(totalTime = totalTime * 1000L, modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center))
        }
    }
}

@Composable
fun TrainingInfo(
    exercise: TrainingExercise,
    onOpenModal: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Weight",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "5kg",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Number of repetitions",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = exercise.numberOfRepetitions.toString(),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    ButtonSection(onOpenModal = onOpenModal)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }

    DescriptionSection(description = exercise.description)
    Spacer(Modifier.height(10.dp))
    Text(
        text = "Technique",
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(10.dp))
    TechniqueSection(technique = exercise.technique)
    Spacer(Modifier.height(10.dp))
    Text(
        text = "Common mistakes",
        style = MaterialTheme.typography.h5,
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
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun TechniqueSection(
    technique: String
) {
    Text(
        text = technique,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun DescriptionSection(
    description: String
) {
    Text(
        text = description,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun ButtonSection(
    onOpenModal: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = onOpenModal,
            modifier = Modifier.padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "I managed!", color = Color.White)
        }
        TextButton(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
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