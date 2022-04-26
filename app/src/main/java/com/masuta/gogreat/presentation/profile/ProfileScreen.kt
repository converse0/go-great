package com.masuta.gogreat.presentation.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.presentation.BottomNavigationItem
import com.masuta.gogreat.presentation.components.BottomMenuBar
import com.masuta.gogreat.presentation.components.InputTextField
import com.masuta.gogreat.presentation.components.MainTextButton
import com.masuta.gogreat.presentation.components.SliderWithLabelUserActivity
import com.masuta.gogreat.presentation.ui.theme.Green
import com.masuta.gogreat.presentation.ui.theme.Red
import com.skydoves.landscapist.glide.GlideImage
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
                .padding(20.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
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

    val userParams = remember { mutableStateOf(ParametersUser()) }

    val fail = remember {
        mutableStateOf(false)
    }

    if (!fail.value) {
        viewModel.getParameters(
            userParams = userParams,
            routeTo = routeTo,
            navController = navController,
            fail = fail
        )
    }

    val lazyListState = rememberLazyListState()

    // Image

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
//        val image = context.contentResolver.openInputStream(uri!!)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 8.dp)
    ) {
        items(1) {
            ProfileAvatar(
                gender = userParams.value.gender,
                imageUri = imageUri,
                bitmap = bitmap,
                context = context,
                viewModel = viewModel,
                profileImg = userParams.value.image
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = userParams.value.username,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Edit photo",
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = TextDecoration.Underline,
                color = Green,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (userParams.value.uid != null) {
                ProfileInfo(
                    lazyListState = lazyListState,
                    viewModel = viewModel,
                    userParams = userParams,
                )
            }
            Spacer(Modifier.height(60.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileInfo(
    lazyListState: LazyListState,
    viewModel: ProfileViewModel,
    userParams: MutableState<ParametersUser>,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val state = userParams.value

    val timesEat = remember{ mutableStateOf(state.eat.toString()) }
    val age = remember{ mutableStateOf(state.age.toString()) }
    val weight = remember{ mutableStateOf(state.weight.toString()) }
    val height = remember{ mutableStateOf(state.height.toString()) }
    val desiredWeight = remember{ mutableStateOf(state.desiredWeight.toString()) }

    val gender = remember { mutableStateOf(state.gender) }
    val diet = remember { mutableStateOf(state.diet.toFloat()) }
    val activity = remember { mutableStateOf(state.activity.toFloat()) }

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
        Text(
            text = "Physical Activity",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        val listActivity = UserActivity.values().toList()
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

        MainTextButton(
            text = "Save",
            color = Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                val resp = viewModel.updateParams(
                    userParams = state.copy(
                        age = age.value.toInt(),
                        eat = timesEat.value.toInt(),
                        height = height.value.toInt(),
                        weight = weight.value.toInt(),
                        activity = activity.value.toInt(),
                        diet = diet.value.toInt(),
                        desiredWeight = desiredWeight.value.toInt(),
                        gender = gender.value,
                    )
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
        }

//        TextButton(
//            onClick = {
//                CoroutineScope(Dispatchers.Main).launch {
//                    val resp = viewModel.updateParams(
//                        gender = gender.value,
//                        age = age.value.toInt(),
//                        weight = weight.value.toInt(),
//                        height = height.value.toInt(),
//                        activity = activity.value,
//                        diet = diet.value,
//                        timesEat = timesEat.value.toInt(),
//                        desiredWeight = desiredWeight.value.toInt(),
//                        uid = uid.value
//                    )
//                    if (resp.isNotEmpty()) {
//                        Toast.makeText(
//                            context,
//                            resp,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Update user parameters Success",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    lazyListState.scrollToItem(0)
//                }
//
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Red),
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 20.dp)
//        ) {
//            Text(
//                text = "Save",
//                color = Color.White,
//                modifier = Modifier.padding(vertical = 16.dp),
//            )
//        }
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
    gender: Int,
    imageUri: Uri?,
    bitmap: MutableState<Bitmap?>,
    context: Context,
    viewModel: ProfileViewModel,
    profileImg: String?
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
                if (viewModel.isUploadImage) {
                    imageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value = MediaStore.Images
                                .Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { btm ->
                            viewModel.uploadImage(btm.asImageBitmap())
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(shape = CircleShape)
                            )
                        }
                    }
                }
                if (bitmap.value != null) {
                    Image(
                        bitmap = bitmap.value!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape)
                    )
                }
            }
            if (profileImg == null || profileImg.isEmpty() && bitmap.value == null) {
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
            } else {
                GlideImage(
                    imageModel = profileImg,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(shape = CircleShape)
                )
            }
        }
    }
}




