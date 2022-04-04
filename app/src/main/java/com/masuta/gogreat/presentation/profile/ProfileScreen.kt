package com.masuta.gogreat.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.components.DropdownDemo

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProfileSection()
    }
}

@Composable
fun ProfileSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp, start = 8.dp, end = 8.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 75.dp, end = 8.dp, start = 8.dp, bottom = 8.dp),
            ) {
                Text(
                    text = "UserName",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Divider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ProfileInfo(title = "Height(sm)", info = "199")
                    ProfileInfo(title = "Sex", info = "Male")
                    ProfileInfo(title = "Weight(kg)", info = "88.7")
                }
            }
        }
        ProfileAvatar(
            image = R.drawable.avatar_male
        )
    }
}

@Composable
fun ProfileAvatar(
    image: Int
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(color = Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Avatar Female",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = Color.Transparent)
                .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
        )
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
