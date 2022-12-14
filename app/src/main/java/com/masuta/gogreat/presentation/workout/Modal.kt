package com.masuta.gogreat.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.SliderWithLabel
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.ui.theme.Green

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StartTrainingModal(
    viewModel: StartTrainingViewModel,
    weight: MutableState<String>,
    time: MutableState<Int>,
    durationTime: MutableState<Float>,
    numberOfSets: MutableState<String>,
    numberOfRepetitions: MutableState<String>,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val relax = viewModel.listRelax
    val duration = viewModel.listDuration

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
            containerColor = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 80.dp
                    )
                ){
                    item {
//                        Spacer(Modifier.height(10.dp))
//                        InputTextField(
//                            text = "Weight",
//                            value = weight.value,
//                            keyboardController = keyboardController,
//                            onChangeValue = { weight.value = it },
//                            keyboardType = KeyboardType.Number
//                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Choose relax time, sec",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                        SliderWithLabel(
                            value = 0f,
                            selectedItem = time,
                            valueRange = 0f..relax.size.minus(1).toFloat(),
                            finiteEnd = true,
                            items = relax
                        )
                        Spacer(Modifier.height(10.dp))
                        if (viewModel.currentExercise.value.type == "other") {
                            Text(
                                text = "Choose duration time, sec",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                            SliderWithLabelUserActivity (
                                selectedItem = durationTime,
                                valueRange = 0f..duration.size.minus(1).toFloat(),
                                items = duration
                            )
                            Spacer(Modifier.height(10.dp))
                        }
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
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
                TextButton(
                    onClick =  onSave,
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 30.dp,
                            start = 16.dp,
                            bottom = 16.dp,
                            end = 16.dp,
                        )
                        .align(Alignment.BottomCenter)
                    ,
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