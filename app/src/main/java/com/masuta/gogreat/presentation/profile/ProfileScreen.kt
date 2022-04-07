package com.masuta.gogreat.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.presentation.components.DropdownDemo
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "Profile",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProfileSection(viewModel)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileSection(viewModel: ProfileViewModel) {
    val timesEat = remember{ mutableStateOf("2-3 times a day") }
    val age = remember{ mutableStateOf("25") }
    val weight = remember{ mutableStateOf("70") }
    val height = remember{ mutableStateOf("170") }
    val desiredWeight = remember{ mutableStateOf("70") }

    val gender = remember { mutableStateOf(0) }
    val diet = remember { mutableStateOf(UserDiet.BALANCED) }
    val activity = remember { mutableStateOf(UserActivity.BASIC) }

    val scope = rememberCoroutineScope()
    scope.launch {
        val params = viewModel.getParameters()
        timesEat.value = params.eat.toString()
        age.value = params.age.toString()
        weight.value = params.weight.toString()
        height.value = params.height.toString()
        desiredWeight.value = params.desiredWeight.toString()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(1) {
            ProfileAvatar()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Maria",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInfo(
                timesEat = timesEat,
                age = age,
                weight = weight,
                height = height,
                desiredWeight = desiredWeight,
                gender = gender,
                diet = diet,
                activity = activity
            )
            Spacer(Modifier.height(40.dp))

            TextButton(
                onClick = {
                   viewModel.setParameters(age.value.toIntOrNull(),
                       weight.value.toInt(),
                       height.value.toInt(),
                       desiredWeight.value.toInt(),
                       timesEat.value.toInt())
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp)
            ) {
                Text(text = "Save", color = Color.White, modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileInfo(
    timesEat: MutableState<String>,
    age: MutableState<String>,
    weight: MutableState<String>,
    height: MutableState<String>,
    desiredWeight: MutableState<String>,
    gender: MutableState<Int>,
    activity: MutableState<UserActivity>,
    diet: MutableState<UserDiet>
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(10.dp))
        GenderChoisen(selected = gender.value, onGenderSelect = { gender.value = it })
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Age",
            value = age.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { age.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Weight",
            value = weight.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { weight.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Height",
            value = height.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { height.value = it }
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Physical activity",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(20.dp))
        PhysicalActivitySection(selected = activity.value, onPhysicalActivitySelect = { activity.value = it })
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Physical activity",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(20.dp))
        DietSection(selected = diet.value, onDietSelect = { diet.value = it })
        Spacer(Modifier.height(20.dp))
        InputTextField(
            text = "How often do you prefer to eat?",
            value = timesEat.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { timesEat.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Desired weight",
            value = desiredWeight.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { desiredWeight.value = it }
        )
    }
}

@Composable
fun DietSection(
    selected: UserDiet,
    onDietSelect: (UserDiet) -> Unit
) {
    Column {
        DefaultRadioButton(
            text = UserDiet.BALANCED.value,
            selected = selected == UserDiet.BALANCED,
            onSelect = { onDietSelect(UserDiet.BALANCED) })
        DefaultRadioButton(
            text = UserDiet.LOW_FAT.value,
            selected = selected == UserDiet.LOW_FAT,
            onSelect = { onDietSelect(UserDiet.LOW_FAT) })
        DefaultRadioButton(
            text = UserDiet.LOW_CARBS.value,
            selected = selected == UserDiet.LOW_CARBS,
            onSelect = { onDietSelect(UserDiet.LOW_CARBS) })
        DefaultRadioButton(
            text = UserDiet.LOW_PROTEIN.value,
            selected = selected == UserDiet.LOW_PROTEIN,
            onSelect = { onDietSelect(UserDiet.LOW_PROTEIN) })
    }
}

@Composable
fun PhysicalActivitySection(
    selected: UserActivity,
    onPhysicalActivitySelect: (UserActivity) -> Unit
) {
    Column() {
        DefaultRadioButton(
            text = UserActivity.BASIC.value,
            selected = selected == UserActivity.BASIC,
            onSelect = { onPhysicalActivitySelect(UserActivity.BASIC) })
        DefaultRadioButton(
            text = UserActivity.LOW.value,
            selected = selected == UserActivity.LOW,
            onSelect = { onPhysicalActivitySelect(UserActivity.LOW) })
        DefaultRadioButton(
            text = UserActivity.LIGHT.value,
            selected = selected == UserActivity.LIGHT,
            onSelect = { onPhysicalActivitySelect(UserActivity.LIGHT) })
        DefaultRadioButton(
            text = UserActivity.MEDIUM.value,
            selected = selected == UserActivity.MEDIUM,
            onSelect = { onPhysicalActivitySelect(UserActivity.MEDIUM) })
        DefaultRadioButton(
            text = UserActivity.HIGH.value,
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
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun LineSelectPoint() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                color = Color.Gray
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = Color.DarkGray)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.Gray)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.Gray)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.Gray)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            backgroundColor = Color.Gray,
            shape = RoundedCornerShape(14.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 10.dp)
            ) {
                Text(text = "Basic", fontSize = 14.sp)
                Text(text = "No Activity", fontSize = 12.sp)
            }
        }
    }
}


@Composable
fun ProfileAvatar() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.Gray)
            )
            ChangeProfileAvatarButton(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}
@Composable
fun ChangeProfileAvatarButton(
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
    ){
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    SportTheme() {
        ProfileScreen(
            viewModel = viewModel()
        )
    }
}
