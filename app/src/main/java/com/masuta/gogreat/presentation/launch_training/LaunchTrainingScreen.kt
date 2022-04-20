package com.masuta.gogreat.presentation.launch_training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.masuta.gogreat.R

@Composable
fun LaunchTrainingScreen() {

    var click by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(bottom = 50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
        }
        ListExercises(onClickVideo = { click = true })
    }
    if (click) {

        Box(
            modifier = Modifier
                .fillMaxSize()
            ) {
            Surface(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.8f)
            ){

            }
            IconButton(onClick = { click = false }, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.training),
                contentDescription = "Video",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ListExercises(
    onClickVideo: () -> Unit
) {
    LazyColumn {
        items(4) {
            ItemExercise(onClickVideo = onClickVideo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemExercise(
    onClickVideo: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp),
//        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.training),
                contentDescription = "Training",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onClickVideo() },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Работа со штангой",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .border(width = 2.dp, color = Color.Green, shape = CircleShape)
                            .clip(CircleShape)
                            .background(color = Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.Green
                        )
                    }
                }
            }
            Text(
                text = "Количество подходов: 20",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = "Количество повторений в каждом подходе: 20",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = "Вес в каждом повторении: 20 килограмм",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = "Время отдыха между повторениями: 90 секунд",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
