package com.masuta.gogreat.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masuta.gogreat.presentation.ui.theme.Green

@Composable
fun Timer(
    totalTime: Long,
    onTimerEnd: () -> Unit,
    modifier: Modifier = Modifier,
    startTimer: Boolean = false,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColor: Color = Color(0xFF37B900),
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
) {
    KeepScreenOn()

    val viewModel: TimerViewModel = hiltViewModel()

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var started by remember {
        mutableStateOf(true)
    }
    var textBtn by remember {
        mutableStateOf("Pause")
    }

    val ctx = LocalContext.current
    val text = remember {
        mutableStateOf("")
    }
    val iconChoose = fun (): ImageVector {
        return if (started) {
            Icons.Default.Pause
        } else {
            Icons.Default.PlayArrow
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
                    sweepAngle = 360f * (viewModel.currSec *  1000).toLong()/(totalTime),
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            var inniter by remember {
                mutableStateOf(0)
            }

            if (text.value==""&& inniter == 0) {
                inniter+=1
                viewModel.init((totalTime / 1000L).toInt())
                viewModel.start(text, ctx, onTimerEnd = onTimerEnd)
            }

            Text(
                text = text.value,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
       }
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TimerButtonWithText(
                text = textBtn,
                icon = iconChoose(),
                color = Green,
                onClick = {
                    if (started) {
                        viewModel.pause()
                        started = false
                        textBtn = "Start"
                    } else {
                        viewModel.start(text, ctx, onTimerEnd = onTimerEnd)
                        started = true
                        textBtn = "Pause"
                    }

                }
            )
            TimerButtonWithText(
                text = "Stop",
                icon = Icons.Default.Stop,
                color = Color.Red,
                onClick = {
                    started = false
                    textBtn = "Start"
                    text.value = (totalTime / 1000L).toInt().toString()
                    viewModel.stop(onTimerEnd = {  })
                    viewModel.init((totalTime / 1000L).toInt())
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

@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}