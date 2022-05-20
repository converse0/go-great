package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.model.UserActivity
import com.masuta.gogreat.core.model.UserDiet
import com.masuta.gogreat.core.model.userToken
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.ui.theme.Red

@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    if (userToken == null) navController.navigate("sign-in") else
                        navController.navigate("main")
                }

            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "About you",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AboutForm(
            viewModel = viewModel,
            navController = navController
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AboutForm(
    viewModel: AboutViewModel,
    navController: NavHostController
) {
    val age = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val timesEat = remember { mutableStateOf("") }
    val desiredWeight = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf(0) }
    val physicalActivity = remember { mutableStateOf(0.toFloat()) }
    val diet = remember { mutableStateOf(0.toFloat()) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val listActivity = UserActivity.values().toList()
    val listDiet = UserDiet.values().toList()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            item {
                Text(
                    text = "Hello! To help us create the best workout diary for you, please tell us a few words about you and your preferences",
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Gender",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
                GenderChoisen(
                    selected = gender.value,
                    onGenderSelect = { gender.value = it }
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Age",
                    value = age.value,
                    keyboardController = keyboardController,
                    keyboardType = KeyboardType.Number,
                    onChangeValue = { age.value = it},
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Weight",
                    value = weight.value,
                    keyboardController = keyboardController,
                    keyboardType = KeyboardType.Number,
                    onChangeValue = { weight.value = it},
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Height",
                    value = height.value,
                    keyboardController = keyboardController,
                    keyboardType = KeyboardType.Number,
                    onChangeValue = { height.value = it},
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Physical activity",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(20.dp))
                SliderWithLabelUserActivity(
                    selectedItem = physicalActivity,
                    valueRange = 0f..listActivity.size.minus(1).toFloat(),
                    labelMinWidth = 100.dp,
                    items = listActivity
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Diet",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(20.dp))
                SliderWithLabelUserActivity(
                    selectedItem = diet,
                    valueRange = 0f..listDiet.size.minus(1).toFloat(),
                    labelMinWidth = 100.dp,
                    items = listDiet
                )
                Spacer(Modifier.height(20.dp))
                InputTextField(
                    text = "How often do you prefer to eat?",
                    value = timesEat.value,
                    keyboardController = keyboardController,
                    keyboardType = KeyboardType.Number,
                    onChangeValue = { timesEat.value = it},
                )
                Spacer(Modifier.height(10.dp))
                InputTextField(
                    text = "Desired weight",
                    value = desiredWeight.value,
                    keyboardController = keyboardController,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    onChangeValue = { desiredWeight.value = it},
                )
            }
        }
        MainTextButton(
            text = "Save",
            color = Red,
            enabled = age.value.isNotEmpty() && weight.value.isNotEmpty() && height.value.isNotEmpty()
                    && desiredWeight.value.isNotEmpty() && timesEat.value.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    top = 40.dp,
                    bottom = 20.dp
                )
        ) {
            viewModel.setParameters(
                age = if(age.value.isNotEmpty()) age.value.toIntOrNull() else 0,
                weight = if (weight.value.isNotEmpty()) weight.value.toInt() else 0,
                height = if (height.value.isNotEmpty()) height.value.toInt() else 0,
                desiredWeight = if (desiredWeight.value.isNotEmpty()) desiredWeight.value.toInt() else 0 ,
                timesEat = if(timesEat.value.isNotEmpty()) timesEat.value.toInt() else 0,
                diet = diet.value.toInt(),
                activity = physicalActivity.value.toInt(),
                gender = gender.value
            )
            navController.navigate("main")
        }
    }
}

@Composable
fun GenderChoisen(
    selected: Int,
    onGenderSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "Male", selected = selected == 0, onSelect = { onGenderSelect(0) })
        DefaultRadioButton(text = "Female", selected = selected == 1, onSelect = { onGenderSelect(1) })
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}