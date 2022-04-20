package com.masuta.gogreat.presentation.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.masuta.gogreat.presentation.ui.theme.SportTheme
//
//@Composable
//fun DropdownDemo(items: List<Int>, selected: MutableState<Int>) {
//    var expanded by remember { mutableStateOf(false) }
//    val disabledValue = "B"
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.TopStart)
//        .border(1.dp, shape = RoundedCornerShape(10.dp), color = Color.Gray)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(onClick = { expanded = true })
//                .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp))
//                .padding(10.dp)
//        ) {
//            Text(text = selected.value.toString())
//            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
//        }
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(color = Color.White)
//        ) {
//            items.forEachIndexed { index, s ->
//                DropdownMenuItem(
//                    onClick = {
//                        selected.value = s
//                        expanded = false
//                    },
//                    modifier = Modifier
//                        .background(color = if (selected.value == s) Color.Green else Color.Transparent)
//                ) {
//                    val disabledText = if (s.toString() == disabledValue) {
//                        " (Disabled)"
//                    } else {
//                        ""
//                    }
//                    Text(text = s.toString() + disabledText)
//                }
//                if (index != items.size - 1) {
//                    Divider()
//                }
//            }
//        }
//    }
//}