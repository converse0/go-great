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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masuta.gogreat.presentation.components.DropdownDemo
import com.masuta.gogreat.presentation.ui.theme.SportTheme

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

@Composable
fun ProfileSection(viewModel: ProfileViewModel) {

    val timesEat = remember{ mutableStateOf("2-3 times a day") }
    val age = remember{ mutableStateOf("25") }
    val weight = remember{ mutableStateOf("70") }
    val height = remember{ mutableStateOf("170") }
    val desiredWeight = remember{ mutableStateOf("70") }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
    ) {
        items(1) {
            ProfileAvatar()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Maria",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInfo(timesEat, age, weight, height, desiredWeight)
            Spacer(Modifier.height(40.dp))
            TextButton(
                onClick = {
                   viewModel.setParameters(age.value.toInt(),
                       weight.value.toInt(),
                       height.value.toInt(),
                       desiredWeight.value.toInt(),
                       timesEat.value.toInt())
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)
            ) {
                Text(text = "Save", color = Color.White, modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

@Composable
fun ProfileInfo(timesEat: MutableState<String>, age: MutableState<String>, weight: MutableState<String>, height: MutableState<String>, desiredWeight: MutableState<String>) {



    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            /* CheckBox */
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Age",
            style = MaterialTheme.typography.body1
        )
        OutlinedTextField(
            value = age.value,
            onValueChange = {age.value = it},
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged { focused ->
                    if (focused.isFocused) {
                        age.value = ""
                    }},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(10.dp))
        Text(text = "Weight")
        OutlinedTextField(
            value = weight.value,
            onValueChange = {weight.value = it},
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged { focused ->
                    if (focused.isFocused) {
                        weight.value = ""
                    }},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(10.dp))
        Text(text = "Height")
        OutlinedTextField(
            value = height.value,
            onValueChange = {height.value = it},
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged { focused ->
                if (focused.isFocused) {
                    height.value = ""
                }},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Physical activity",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(20.dp))
        LineSelectPoint()
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Physical activity",
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(20.dp))
        LineSelectPoint()
        Spacer(Modifier.height(20.dp))
        Text(
            text = "How often do you prefer to eat?",
            style = MaterialTheme.typography.body1
        )
        OutlinedTextField(
            value = timesEat.value,
            onValueChange = {timesEat.value = it},
            modifier = Modifier.fillMaxWidth().onFocusChanged { focused ->
                if (focused.isFocused) {
                    timesEat.value = ""
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Desired weight",
            style = MaterialTheme.typography.body1
        )
        OutlinedTextField(
            value = desiredWeight.value,
            onValueChange = {desiredWeight.value = it},
            modifier = Modifier.fillMaxWidth().onFocusChanged { focused ->
                if (focused.isFocused) {
                    desiredWeight.value = ""
                }
            }
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

@Composable
fun ProfileInfo(
    title: String,
    info: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
            color = Color.Black.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = info,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
fun ListItem(profile: Profile) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
    }
}

enum class Sex(sex:String) {
    MALE("Male"), FEMALE("Female")
}

data class Profile(
    var sex: Sex? = null, var old: Int? = null,
    var weight: Float? = null, var height: Float? = null,
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun SexSelector(sexChousen: MutableState<Sex>) {

    val index = mutableStateOf(0)
    Spacer(modifier = Modifier.height(15.dp))
    Row(Modifier.padding(start = 0.dp, top = 10.dp, end = 25.dp)) {
        DropdownDemo(listOf("Male","Female"), index)
    }
    val sex = when(index.value) {
        0 -> Sex.MALE
        1-> Sex.FEMALE
        else -> Sex.MALE
    }
    sexChousen.value = sex

}

@Composable
fun WeightSelector(weight: MutableState<Double>) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 0.dp, top = 10.dp,)
    ){
        var text: Int? by remember { mutableStateOf(null) }
        var fr: Int? by remember { mutableStateOf(null) }
        var finalWeight: Double? by rememberSaveable {
            mutableStateOf(null)
        }
        OutlinedTextField(
            value = text?.toString() ?: "",
            modifier = Modifier.size(width = 128.dp, height = 64.dp),
            onValueChange = {
                text = if (it.isNotEmpty()) it.toInt() else null
                finalWeight = (text?.toDouble() ?: 0.0) + (".${fr ?: 0}".toDouble())
            },
            label = { Text("weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = ".")
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            value = fr?.toString() ?: "",
            maxLines = 1,
            modifier = Modifier.size(width = 128.dp, height = 64.dp),
            onValueChange = {
                fr = if (it.isNotEmpty()) it.toInt() else null
                finalWeight = (text?.toDouble() ?: 0.0) + (".${fr ?: 0}".toDouble())
            },
            label = { Text("fractional") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        weight.value = finalWeight ?: 0.0
        Text(
            text = weight.value.toString()
        )
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageList() {
    Column {
        var pickerValue by remember { mutableStateOf(0) }
        val profile = Profile()
        Card(modifier = Modifier.fillMaxWidth(),
            elevation = 12.dp,
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier.padding(55.dp),
                verticalArrangement = Arrangement.SpaceAround) {

                var age: Int? by remember {
                    mutableStateOf(0)
                }
                OutlinedTextField(
                    value = age?.toString() ?: "",
                    maxLines = 1,
//                        modifier = Modifier.size(width = 70.dp, height = 70.dp),
                    onValueChange = {
                        age = if (it.isNotEmpty()) it.toInt() else null
                    },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                val weight: MutableState<Double> = mutableStateOf(0.0)
                WeightSelector(weight)
                val sexChousen: MutableState<Sex> = mutableStateOf(Sex.MALE)

                SexSelector(sexChousen)
            }

        }

    }
}

@Preview
@Composable
fun ProfilePreview() {
    SportTheme {
       // ProfileScreen()
    }
}