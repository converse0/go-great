package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import kotlinx.coroutines.delay
import java.lang.Math.*
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Timer(
    totalTime: Long,
    onAlarmSound: () -> Unit,
    onTimerEnd: () -> Unit,
    modifier: Modifier = Modifier,
    startTimer: Boolean = false,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColor: Color = Color(0xFF37B900),
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
) {
//    val context = LocalContext.current

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(initialValue)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(startTimer)
    }

    if (currentTime == 5000L) {
//            playSound(context)
        onAlarmSound()
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
        if (currentTime/1000L == 0L) {
            delay(1000L)
            onTimerEnd()
            value = 1f
            currentTime = totalTime
            isTimerRunning = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .onSizeChanged {
                    size = it
                }
        ) {
            Canvas(modifier = modifier) {
                drawArc(
                    color = inactiveBarColor,
                    startAngle = -270f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = activeBarColor,
                    startAngle = -270f,
                    sweepAngle = 360f * value,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = (currentTime / 1000L).toString(),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TimerButtonWithText(
                text = if(isTimerRunning) "Pause" else "Start",
                icon = if(isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                color = Color.Green,
                onClick = {
                    if(currentTime <= 0L) {
                        currentTime = totalTime
                        isTimerRunning = true
                    } else {
                        isTimerRunning = !isTimerRunning
                    }
                }
            )
            TimerButtonWithText(
                text = "Stop",
                icon = Icons.Default.Stop,
                color = Color.Red,
                onClick = {
                    isTimerRunning = false
                    currentTime = totalTime
                    value = 1f
                }
            )
        }
    }
}

@Composable
fun TimerButtonWithText(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = text, modifier = Modifier.padding(vertical = 8.dp))
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color = color, shape = CircleShape)
        ) {
            Icon(icon, contentDescription = text, tint = Color.White)
        }
    }
}

//@Preview
//@Composable
//fun TimerPreview() {
//    SportTheme() {
//        Timer(
//            totalTime = 10L * 1000L,
//            inactiveBarColor = Color.DarkGray,
//            activeBarColor = Color(0xFF37B900),
//            modifier = Modifier.size(200.dp).padding(bottom = 60.dp)
//        )
//    }
//}