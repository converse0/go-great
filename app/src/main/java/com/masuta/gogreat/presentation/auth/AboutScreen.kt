package com.masuta.gogreat.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.masuta.gogreat.presentation.profile.LineSelectPoint
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
                style = MaterialTheme.typography.h4,
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


    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = "Hello, Maria! To help us create the best workout diary for you, please tell us a few words about you and your preferences"
            )
            Spacer(Modifier.height(10.dp))
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
                onValueChange = { age.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused ->
                        if (focused.isFocused) {
                            age.value = ""
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(10.dp))
            Text(text = "Weight")
            OutlinedTextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused ->
                        if (focused.isFocused) {
                            weight.value = ""
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(10.dp))
            Text(text = "Height")
            OutlinedTextField(
                value = height.value,
                onValueChange = { height.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused ->
                        if (focused.isFocused) {
                            height.value = ""
                        }
                    },
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
                onValueChange = { timesEat.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused ->
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
                onValueChange = { desiredWeight.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused ->
                        if (focused.isFocused) {
                            desiredWeight.value = ""
                        }
                    }
            )
            TextButton(
                onClick = {
                    viewModel.setParameters(
                        age.value.toIntOrNull(),
                        weight.value.toInt(),
                        height.value.toInt(),
                        desiredWeight.value.toInt(),
                        timesEat.value.toInt()
                    )
                    navController.navigate("main")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 60.dp)
            ) {
                Text(
                    text = "Save",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    SportTheme() {
        AboutScreen(viewModel = viewModel(), navController = NavHostController(LocalContext.current))
    }
}