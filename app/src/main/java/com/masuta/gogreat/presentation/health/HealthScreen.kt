package com.masuta.gogreat.presentation.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.diet.DietScreen
import com.masuta.gogreat.presentation.ui.theme.SportTheme

@Composable
fun HealthScreen(
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "My Health",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sport_health),
                    contentDescription = "Health",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Coming soon!",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W700
            )
        }
    }
}