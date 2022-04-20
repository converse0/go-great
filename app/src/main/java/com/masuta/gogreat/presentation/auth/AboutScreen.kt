package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.components.SliderWithLabelUserDiet
import com.masuta.gogreat.presentation.profile.LineSelectPoint
import com.masuta.gogreat.presentation.profile.firstCharToUpperCase
import com.masuta.gogreat.presentation.profile.normalizeString
import com.masuta.gogreat.presentation.ui.theme.Red
import com.masuta.gogreat.presentation.ui.theme.SportTheme

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
                    navController.navigate("sign-up")
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
    var physicalActivity = remember { mutableStateOf(UserActivity.BASIC) }
    var diet = remember { mutableStateOf(UserDiet.BALANCED) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Hello, Maria! To help us create the best workout diary for you, please tell us a few words about you and your preferences"
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Gender",
                style = MaterialTheme.typography.bodySmall
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
//            PhysicalActivitySection(
//                selected = physicalActivity,
//                onPhysicalActivitySelect = { physicalActivity = it }
//            )
            val listActivity = UserActivity.values().toList()
            SliderWithLabelUserActivity(
                value = 0f,
                selectedItem = physicalActivity,
                valueRange = 0f..listActivity.size.minus(1).toFloat(),
                finiteEnd = true,
                items = listActivity
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Diet",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(20.dp))
//            DietSection(
//                selected = diet,
//                onDietSelect = { diet = it}
//            )
            val listDiet = UserDiet.values().toList()
            SliderWithLabelUserDiet(
                value = 0f,
                selectedItem = diet,
                valueRange = 0f..listDiet.size.minus(1).toFloat(),
                finiteEnd = true,
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
                onChangeValue = { desiredWeight.value = it},
            )
        }
        TextButton(
            onClick = {
                viewModel.setParameters(
                    age = if(age.value.isNotEmpty()) age.value.toIntOrNull() else 0,
                    weight = if (weight.value.isNotEmpty()) weight.value.toInt() else 0,
                    height = if (height.value.isNotEmpty()) height.value.toInt() else 0,
                    desiredWeight = if (desiredWeight.value.isNotEmpty()) desiredWeight.value.toInt() else 0 ,
                    timesEat = if(timesEat.value.isNotEmpty()) timesEat.value.toInt() else 0,
                    diet = diet.value.value,
                    activity = physicalActivity.value.value,
                    gender = gender.value
                )
                navController.navigate("main")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Red),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    top = 40.dp,
                    bottom = 20.dp
                )
        ) {
            Text(
                text = "Save",
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
fun DietSection(
    selected: UserDiet,
    onDietSelect: (UserDiet) -> Unit
) {
    Column {
        DefaultRadioButton(text = UserDiet.BALANCED.toString().lowercase(), selected = selected == UserDiet.BALANCED, onSelect = { onDietSelect(UserDiet.BALANCED) })
        DefaultRadioButton(text = UserDiet.LOW_FAT.toString().lowercase(), selected = selected == UserDiet.LOW_FAT, onSelect = { onDietSelect(UserDiet.LOW_FAT) })
        DefaultRadioButton(text = UserDiet.LOW_CARBS.toString().lowercase(), selected = selected == UserDiet.LOW_CARBS, onSelect = { onDietSelect(UserDiet.LOW_CARBS) })
        DefaultRadioButton(text = UserDiet.LOW_PROTEIN.toString().lowercase(), selected = selected == UserDiet.LOW_PROTEIN, onSelect = { onDietSelect(UserDiet.LOW_PROTEIN) })
    }
}

@Composable
fun PhysicalActivitySection(
    selected: UserActivity,
    onPhysicalActivitySelect: (UserActivity) -> Unit
) {
    val list = UserActivity.values().toList()

    println(list)

    Column() {
        DefaultRadioButton(text = UserActivity.BASIC.toString()
            .lowercase().firstCharToUpperCase()
            .normalizeString(),
            selected = selected == UserActivity.BASIC,
            onSelect = { onPhysicalActivitySelect(UserActivity.BASIC) })
        DefaultRadioButton(text = UserActivity.LOW.toString()
            .lowercase().firstCharToUpperCase()
            .normalizeString(),
            selected = selected == UserActivity.LOW,
            onSelect = { onPhysicalActivitySelect(UserActivity.LOW) })
        DefaultRadioButton(text = UserActivity.LIGHT.toString().lowercase()
            .firstCharToUpperCase().normalizeString(),
            selected = selected == UserActivity.LIGHT,
            onSelect = { onPhysicalActivitySelect(UserActivity.LIGHT) })
        DefaultRadioButton(text = UserActivity.MEDIUM.toString().lowercase()
            .firstCharToUpperCase().normalizeString(),
            selected = selected == UserActivity.MEDIUM,
            onSelect = { onPhysicalActivitySelect(UserActivity.MEDIUM) })
        DefaultRadioButton(text = UserActivity.HIGH.toString().lowercase()
            .firstCharToUpperCase().normalizeString(),
            selected = selected == UserActivity.HIGH,
            onSelect = { onPhysicalActivitySelect(UserActivity.HIGH) })
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
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    SportTheme() {
        AboutScreen(viewModel = viewModel(), navController = NavHostController(LocalContext.current))
    }
}