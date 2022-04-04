package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun DropdownDemo(items: List<String>, indexSel: MutableState<Int> = mutableStateOf(0)) {
    var expanded by remember { mutableStateOf(false) }
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)
        .border(1.dp, shape = RoundedCornerShape(10.dp), color = Color.Gray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(items[selectedIndex])
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        indexSel.value = index
                        expanded = false
                    },
                    modifier = Modifier
                        .background(color = if (selectedIndex == index) Color.Green else Color.Transparent)
                ) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
                if (index != items.size - 1) {
                    Divider()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DropDownPreview() {
    val list = listOf("Hello", "World", "New", "Party")
    SportTheme {
        DropdownDemo(items = list)
    }
}
