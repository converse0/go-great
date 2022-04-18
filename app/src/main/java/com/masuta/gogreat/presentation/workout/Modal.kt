package com.masuta.gogreat.presentation.workout

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartTrainingModal(
    weight: MutableState<String>,
    time: MutableState<String>,
    numberOfSets: MutableState<String>,
    numberOfRepetitions: MutableState<String>,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
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
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Weight",
                    value = weight.value,
                    keyboardController = keyboardController,
                    onChangeValue = { weight.value = it },
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Time",
                    value = time.value,
                    keyboardController = keyboardController,
                    onChangeValue = { time.value = it },
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Number of sets",
                    value = numberOfSets.value,
                    keyboardController = keyboardController,
                    onChangeValue = { numberOfSets.value = it },
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Number of repetitions",
                    value = numberOfRepetitions.value,
                    keyboardController = keyboardController,
                    onChangeValue = { numberOfRepetitions.value = it },
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(30.dp))
                TextButton(
                    onClick =  onSave,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth(),
//                    enabled = false
                ) {
                    Text(
                        text = "OK!",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FinalModal() {
    val context = LocalContext.current


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
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Spacer(Modifier.height(10.dp))
                Text(text = "Gratz", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}