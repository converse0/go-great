package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.presentation.BottomNavigationItem

@Composable
fun BottomMenuBar(
    navController: NavHostController,
    selected: String,
    onSelect: (String) -> Unit,
    menuItems: List<BottomNavigationItem>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth().background(color = Color.White)
    ) {
        menuItems.forEach { item ->
            val color = if (selected == item.route) Color.Green else Color.Gray
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onSelect(item.route)
                        navController.navigate(item.route)
                    }
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(color)
                        .height(2.dp)
                )
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(vertical = 8.dp).size(20.dp)
                )
                Text(
                    text = item.title,
                    color = color,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}


// Old bottom bar

//BottomAppBar(
//backgroundColor = Color.LightGray
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceAround,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        menuItems.forEach { item ->
//            IconButton(onClick = {
//                navController.navigate(item.route)
//                onSelect(item.route)
//            }) {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.route,
//                    tint = if (item.route == selected) Color.Green else Color.Black
//                )
//            }
//        }
//    }
//}
