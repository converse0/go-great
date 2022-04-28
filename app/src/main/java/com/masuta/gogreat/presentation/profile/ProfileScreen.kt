package com.masuta.gogreat.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
import kotlinx.coroutines.*


fun String.firstCharToUpperCase(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
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
        Toast.makeText(
            LocalContext.current,
            viewModel.errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    Scaffold(
        bottomBar = {
            BottomMenuBar(
                navController = navController,
                selected = selected,
                onSelect = onSelect,
                menuItems = menuItems
            )
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

    val userParams = remember { viewModel.userParams }

    val fail = remember {
        mutableStateOf(false)
    }

    if (!fail.value) {
        viewModel.getParameters(
            routeTo = routeTo,
            navController = navController,
            fail = fail
        )
    }

    val lazyListState = rememberLazyListState()

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.isUploadImage.value = true
        imageUri = uri
    }

    userParams.value?.let { params ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(1) {
                println("imageRec: $userParams")
                ProfileAvatar(
                    imageUri = imageUri,
                    bitmap = bitmap,
                    context = context,
                    viewModel = viewModel,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = params.username,
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
                    ProfileInfo(
                        lazyListState = lazyListState,
                        viewModel = viewModel,
                        userParams = params,
                    )
                Spacer(Modifier.height(60.dp))
            }
        }
    }
}

@SuppressLint("CheckResult")
@Composable
fun ProfileAvatar(
    imageUri: Uri?,
    bitmap: MutableState<Bitmap?>,
    context: Context,
    viewModel: ProfileViewModel,
) {

    var image by remember { mutableStateOf(viewModel.userParams.value?.image) }

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
                if (viewModel.isUploadImage.value) {
                    viewModel.isUploadImage.value = false

                    imageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value = MediaStore.Images
                                .Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }
                        bitmap.value?.let { btm ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val resp = viewModel.uploadImage(btm.asImageBitmap())
                                withContext(Dispatchers.Main) {
                                    resp.first?.let { r ->
                                        Toast.makeText(context, r, Toast.LENGTH_LONG ).show()
                                    }?: resp.second?.let { im->
                                        Toast.makeText(context, "Uploaded success", Toast.LENGTH_LONG ).show()
                                        println("${viewModel.userParams.value}")
                                        viewModel.userParams.value = viewModel.userParams.value.apply { image = im +"?${System.currentTimeMillis()}" }
                                        println("${viewModel.userParams.value}")
                                    }
                                }
                            }
                        }
                    }
                }
                if (image != null) {
                    println("PROFILE IMG: $image")
                        GlideImage(
                            imageModel = image,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(modifier = Modifier.matchParentSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            },
                            requestOptions = { RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true) },
                            modifier = Modifier
                                .size(150.dp)
                                .clip(shape = CircleShape)
                        )

                }
            }

        }
    }
}

@SuppressLint("ShowToast")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileInfo(
    lazyListState: LazyListState,
    viewModel: ProfileViewModel,
    userParams: ParametersUser,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val timesEat = remember{ mutableStateOf(userParams.eat.toString()) }
    val age = remember{ mutableStateOf(userParams.age.toString()) }
    val weight = remember{ mutableStateOf(userParams.weight.toString()) }
    val height = remember{ mutableStateOf(userParams.height.toString()) }
    val desiredWeight = remember{ mutableStateOf(userParams.desiredWeight.toString()) }

    val gender = remember { mutableStateOf(userParams.gender) }
    val diet = remember { mutableStateOf(userParams.diet.toFloat()) }
    val activity = remember { mutableStateOf(userParams.activity.toFloat()) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
        Spacer(Modifier.height(10.dp))
        GenderChosen(
            onChooseGender = {},
            selected = gender.value,
            onChooseGender = { gender.value = it }
        )
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
            CoroutineScope(Dispatchers.IO).launch {
                val resp = viewModel.updateParams(
                    userParams = userParams.copy(
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
                withContext(Dispatchers.Main) {
                    resp?.let {
                        if (it.isNotEmpty()) {
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
                    }
                    lazyListState.scrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun GenderChosen(
    selected: Int,
    onChooseGender: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultRadioButton(
            text = "Male",
            selected = selected == 0,
            onSelect = { onChooseGender(0) }
        )
        DefaultRadioButton(
            text = "Female",
            selected = selected == 1,
            onSelect = { onChooseGender(1) }
        )

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

