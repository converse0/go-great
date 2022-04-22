package com.masuta.gogreat.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun String.firstCharToUpperCase(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}

fun String.normalizeString(): String {
    return this.replace("_", " ")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
) {

    if(viewModel.errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current,viewModel.errorMessage,Toast.LENGTH_SHORT ).show()
    }
    Scaffold(
        bottomBar = {
            BottomMenuBar(navController = navController, selected = selected, onSelect = onSelect, menuItems = menuItems)
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileSection(viewModel, navController)
        }
    }
}

val routeTo :(navController: NavHostController, route:String) -> Unit = { navController, route ->
    navController.navigate(route)
}

@Composable
fun ProfileSection(
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    val username = remember { mutableStateOf("") }
    val timesEat = remember{ mutableStateOf("") }
    val age = remember{ mutableStateOf("") }
    val weight = remember{ mutableStateOf("") }
    val height = remember{ mutableStateOf("") }
    val desiredWeight = remember{ mutableStateOf("") }

    val gender = remember { mutableStateOf(0) }
    val diet = remember { mutableStateOf(0.toFloat()) }
    val activity = remember { mutableStateOf(0.toFloat()) }
    val uid = remember{ mutableStateOf("")}
    val fail = remember {
        mutableStateOf(false)
    }
    if (!fail.value) {
        viewModel.getParameters(username, timesEat, age, weight, height, desiredWeight,
            gender,diet, activity, routeTo, navController, fail,uid)
    }

    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(1) {
            ProfileAvatar(gender = gender.value)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = username.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInfo(
                lazyListState = lazyListState,
                viewModel = viewModel,
                timesEat = timesEat,
                age = age,
                weight = weight,
                height = height,
                desiredWeight = desiredWeight,
                gender = gender,
                diet = diet,
                activity = activity,
                uid = uid
            )
            Spacer(Modifier.height(60.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileInfo(
    lazyListState: LazyListState,
    viewModel: ProfileViewModel,
    timesEat: MutableState<String>,
    age: MutableState<String>,
    weight: MutableState<String>,
    height: MutableState<String>,
    desiredWeight: MutableState<String>,
    gender: MutableState<Int>,
    activity: MutableState<Float>,
    diet: MutableState<Float>,
    uid: MutableState<String>
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
        Spacer(Modifier.height(10.dp))
        GenderChosen(selected = gender.value, onGenderChoose = { gender.value = it })
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
//        InputTextField(
//            text = "Physical Activity",
//            value = UserActivity.values()[activity.value]
//                .toString().lowercase()
//                .firstCharToUpperCase().normalizeString(),
//            enabled = false,
//            keyboardController = keyboardController,
//            keyboardType = KeyboardType.Number,
//            onChangeValue = { timesEat.value = it }
//        )
        Text(
            text = "Physical Activity",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        val listActivity = UserActivity.values().toList()
//        val selectedActivity = remember { mutableStateOf(listActivity[activity.value.toInt()]) }
//        println("selectedActivity: ${selectedActivity.value.value}")
//        if (selectedActivity.value.value==0&&activity.value.toInt()>0) {
//            selectedActivity.value = listActivity[activity.value.toInt()]
//        }
//        activity.value = selectedActivity.value.value.toFloat()
        SliderWithLabelUserActivity(
            selectedItem = activity,
            valueRange = 0f..listActivity.size.minus(1).toFloat(),
            items = listActivity
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Diet",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        val listDiet = UserDiet.values().toList()
        SliderWithLabelUserActivity(
            selectedItem = diet,
            valueRange = 0f..listDiet.size.minus(1).toFloat(),
            items = listDiet
        )
//        val selectedDiet = remember { mutableStateOf(listDiet[diet.value]) }
//        diet.value = selectedDiet.value.value
//        SliderWithLabelUserDiet(
//            value = diet.value.toFloat(),
//            selectedItem = selectedDiet,
//            valueRange = 0f..listDiet.size.minus(1).toFloat(),
//            finiteEnd = true,
//            items = listDiet
//        )

//        InputTextField(
//            text = "Diet",
//            value = UserDiet.values()[diet.value]
//                .toString().lowercase()
//                .firstCharToUpperCase().normalizeString(),
//            enabled = false,
//            keyboardController = keyboardController,
//            keyboardType = KeyboardType.Number,
//            onChangeValue = { timesEat.value = it }
//        )
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
        TextButton(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val resp = viewModel.updateParams(
                        gender = gender.value,
                        age = age.value.toInt(),
                        weight = weight.value.toInt(),
                        height = height.value.toInt(),
                        activity = activity.value,
                        diet = diet.value,
                        timesEat = timesEat.value.toInt(),
                        desiredWeight = desiredWeight.value.toInt(),
                        uid = uid.value
                    )
                    if (resp.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            resp,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Update user parameters Success",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    lazyListState.scrollToItem(0)
                }

            },
            colors = ButtonDefaults.buttonColors(containerColor = Red),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

@Composable
fun GenderChosen(
    onGenderChoose: (Int) -> Unit,
    selected: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(text = "Male", selected = selected == 0, onSelect = {onGenderChoose(0)})
        DefaultRadioButton(text = "Female", selected = selected == 1, onSelect = { onGenderChoose(1) })
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
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            containerColor = Color.Gray,
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
fun ProfileAvatar(
    gender: Int
) {
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
            ) {
                if (gender == 0) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_male),
                        contentDescription = "Male Avatar",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape)
                            .padding(10.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_female),
                        contentDescription = "Female Avatar",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}



