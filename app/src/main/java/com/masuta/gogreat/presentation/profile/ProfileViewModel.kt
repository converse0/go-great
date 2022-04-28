package com.masuta.gogreat.presentation.profile

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.handlers.CreateUserParams
import com.masuta.gogreat.domain.handlers.GetUserParams
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val createUserParams: CreateUserParams,
    private val getUserParams: GetUserParams,
    private val repository: ProfileRepository
) :ViewModel() {

    var errorMessage by mutableStateOf("")

    var isUploadImage = mutableStateOf(true)

    var userParams = mutableStateOf(ParametersUser())

    var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    fun getParameters(
        fail: MutableState<Boolean>,
        navController: NavHostController,
        routeTo: (navController: NavHostController, route: String) -> Unit,
    ) {
        if (isDataLoad) {
            viewModelScope.launch {
                val resp = getUserParams()
                if (resp.data != null) {
                    val params = ParametersUser(
                        username = resp.data.username,
                        activity = UserActivity.valueOf(resp.data.activity.uppercase()).value,
                        age = resp.data.age,
                        desiredWeight = resp.data.desiredWeight,
                        diet = UserDiet.valueOf(resp.data.diet.uppercase()).value,
                        eat = resp.data.eat,
                        gender = resp.data.gender,
                        height = resp.data.height,
                        weight = resp.data.weight,
                        uid = resp.data.uid,
                        image = resp.data.image
                    )
                    userParams.value = params
                    repository.setLocalProfileParams(params)
                    isDataLoad = false
                } else if (resp.code!=null) {
                    resp.message?.let { errorMessage = it }
                    fail.value = true
                    when(resp.code) {
                        16 -> routeTo(navController, "sign_in")
                        2, 5, 13 -> routeTo(navController, "about")
                    }
                }
            }
        } else {
            viewModelScope.launch {
                repository.getLocalProfileParams()?.let {
                    userParams.value = it
                }
            }
        }

//        viewModelScope.launch {
//            val resp = getUserParams()
//            if (resp.data!= null) {
//
//                println(resp.data.age)
//                println(resp.data.username)
//                username.value = resp.data.username
//                age.value = resp.data.age.toString()
//                timesEat.value = resp.data.eat.toString()
//                weight.value = resp.data.weight.toString()
//                height.value = resp.data.height.toString()
//                desiredWeight.value = resp.data.desiredWeight.toString()
//                gender.value = resp.data.gender
//                diet.value = UserDiet.valueOf(resp.data.diet.uppercase()).value.toFloat()
//                activity.value = UserActivity.valueOf(resp.data.activity.uppercase()).value.toFloat()
//                println("Activity: ${UserActivity.valueOf(resp.data.activity.uppercase()).value}")
//                resp.data.uid?.let { uid.value = it }
//            } else if (resp.code!=null) {
//                resp.message?.let { errorMessage = it }
//                fail.value = true
//                when(resp.code) {
//                    16 -> routeTo(navController, "sign_in")
//                    2, 5, 13 -> routeTo(navController, "about")
//                }
//            }
//
//        }
}

    fun getParameters(gender: MutableState<Int>) {
        viewModelScope.launch {
            val resp = getUserParams()
            resp.data?.let {
                gender.value = it.gender
            } ?: resp.code?.let {
                resp.message?.let { errorMessage = it }

                gender.value = when(it){
                    16 -> -6
                    2,5 -> 6
                    else -> 777
                }
            }
        }
    }

    suspend fun updateParams(
        userParams: ParametersUser,
    ): String? {
        val params = ParametersUserSet (
            age = userParams.age,
            weight = userParams.weight,
            height = userParams.height,
            desiredWeight = userParams.desiredWeight,
            eat = userParams.eat,
            activity = userParams.activity,
            diet = userParams.diet,
            gender = userParams.gender,
            uid = userParams.uid
        )
        repository.setLocalProfileParams(userParams)
        val resp = repository.updateParameters(params)
        if (resp != null) {
            isDataLoad = true
            return resp
        }
        return null
    }

    suspend fun uploadImage(im: ImageBitmap): String? {
        println("IMAGE BITMAP: $im")
        isDataLoad = true
        val resp = repository.uploadImage(im)
        println("Response: $resp")
        isUploadImage.value = false
        resp.data?.let {
            println("uploadImage: $it")
//            userParams.value = ParametersUser(image = "https://cdn-icons-png.flaticon.com/512/5110/5110429.png")
            userParams.value = userParams.value.apply { image = "https://cdn-icons-png.flaticon.com/512/5110/5110429.png" }

            println("OLD IMAGE LOCAL: ${repository.getLocalProfileParams()?.image}")
            repository.setLocalProfileParams(userParams.value)
//            userParams.value = userParams.value.apply { image = it }
//            repository.setLocalProfileParams(userParams.value)

            println("NEW IMAGE LOCAL: ${repository.getLocalProfileParams()?.image}")
            return null
        } ?: resp.message?.let {
            println("uploadImage error: $it")
            return it
        } ?: resp.code?.let {
            println("uploadImage error: $it")
            return when(it) {
                16 -> "Access Token is expired"
                5,2 -> "profile is not found"
                else -> "Request Time out exceeded"
            }
        }
        return null
    }
}