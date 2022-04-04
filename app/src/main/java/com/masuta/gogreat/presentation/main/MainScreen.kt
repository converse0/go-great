package com.masuta.gogreat.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masuta.gogreat.presentation.ui.theme.Purple200
import com.masuta.gogreat.presentation.ui.theme.SportTheme
import com.masuta.gogreat.presentation.components.DropdownDemo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FormatedText(text:String) {
    val mod = Modifier.padding(top=16.dp, bottom = 16.dp)
    Text(
        text = text,
        modifier = mod,
        color = Purple200
    )
}

@Composable
fun MainScreen() {
    Column(modifier = Modifier.padding(7.dp)) {
        Card(
            elevation = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                FormatedText("Running")
                FormatedText("2022/05/12")
                FormatedText("1h")
                IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(2.dp)
                    .padding(start = 50.dp)) {
                    Icon(imageVector = Icons.Default.PlayArrow,
                        contentDescription = "", tint = Color.Green)
                }
            }


        }
        CountDownTraining(120)
        var text by remember { mutableStateOf("3467") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        DropdownDemo(
            items = listOf("A", "B", "C", "D", "E", "F"),
        )

    }

}




//@Composable
//fun DropdownDemo() {
//    var expanded by remember { mutableStateOf(false) }
//    val items = listOf("A", "B", "C", "D", "E", "F")
//    val disabledValue = "B"
//    var selectedIndex by remember { mutableStateOf(0) }
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.TopStart)) {
//        Text(items[selectedIndex],modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = { expanded = true })
//            .background(
//                Color.Gray
//            ))
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Color.Gray
//                )
//        ) {
//            items.forEachIndexed { index, s ->
//                DropdownMenuItem(onClick = {
//                    selectedIndex = index
//                    expanded = false
//                }) {
//                    val disabledText = if (s == disabledValue) {
//                        " (Disabled)"
//                    } else {
//                        ""
//                    }
//                    Text(text = s + disabledText)
//                }
//            }
//        }
//    }
//}

@Composable
fun CountDownTraining(sec: Int) {
    var text by remember {
        mutableStateOf("Start")
    }
    Spacer(modifier = Modifier.height(50.0.dp))
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()) {
        val col by remember {
            mutableStateOf(Color(0xFF2ABD20))
        }
        FloatingActionButton(
            //col = Color(0xFF0E4E09)
            onClick = {
                val job = CoroutineScope(Dispatchers.Main).launch {
                    val seq = 0..sec
                    for (i in seq.reversed()) {
                        delay(1000)
                        if(i % 10 == 0) println("ping $i")
                        text = i.toString()
                    }
                }
            },
            backgroundColor = col,
            modifier = Modifier.size(150.dp)
        ) {
            Text(text, fontSize = 20.sp)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SportTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainScreen()
        }

    }
}