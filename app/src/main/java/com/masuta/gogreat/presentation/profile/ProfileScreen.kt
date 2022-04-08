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
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.DropdownDemo
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.LightGray
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    menuItems.forEach { item ->
                        IconButton(onClick = {
                            navController.navigate(item.route)
                            onSelect(item.route)
                        }) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.route,
                                tint = if (item.route == selected) Color.Green else Color.Black
                            )
                        }
                    }
                }
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.navigate("main") }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                }
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            ProfileSection(viewModel, navController)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileSection(
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    val timesEat = remember{ mutableStateOf("2-3 times a day") }
    val age = remember{ mutableStateOf("25") }
    val weight = remember{ mutableStateOf("70") }
    val height = remember{ mutableStateOf("170") }
    val desiredWeight = remember{ mutableStateOf("70") }

    val gender = remember { mutableStateOf(0) }
    val diet = remember { mutableStateOf("Balanced") }
    val activity = remember { mutableStateOf("Basic") }

    val scope = rememberCoroutineScope()
    scope.launch {
        val params = viewModel.getParameters()
        params?.let {
            timesEat.value = it.eat.toString()
            age.value = it.age.toString()
            weight.value = it.weight.toString()
            height.value = it.height.toString()
            desiredWeight.value = it.desiredWeight.toString()
//            gender.value = it.gender
//            diet.value = it.diet
//            activity.value = it.activity
        }
        if (params == null) {
            navController.navigate("about")
        }
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
            Spacer(Modifier.height(60.dp))
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
    activity: MutableState<String>,
    diet: MutableState<String>
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
        GenderChoisen(selected = gender.value)
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Age",
            value = age.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            enabled = false,
            onChangeValue = { age.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Weight",
            value = weight.value,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            enabled = false,
            onChangeValue = { weight.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Height",
            value = height.value,
            enabled = false,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { height.value = it }
        )
        Spacer(Modifier.height(20.dp))
        InputTextField(
            text = "Physical Activity",
            value = activity.value,
            enabled = false,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { timesEat.value = it }
        )
        Spacer(Modifier.height(20.dp))
        InputTextField(
            text = "Diet",
            value = diet.value,
            enabled = false,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { timesEat.value = it }
        )
        Spacer(Modifier.height(20.dp))
        InputTextField(
            text = "How often do you prefer to eat?",
            value = timesEat.value,
            enabled = false,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { timesEat.value = it }
        )
        Spacer(Modifier.height(10.dp))
        InputTextField(
            text = "Desired weight",
            value = desiredWeight.value,
            enabled = false,
            keyboardController = keyboardController,
            keyboardType = KeyboardType.Number,
            onChangeValue = { desiredWeight.value = it }
        )
    }
}

@Composable
fun GenderChoisen(
    selected: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "Male", selected = selected == 0)
        DefaultRadioButton(text = "Female", selected = selected == 1)
    }
}

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
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

