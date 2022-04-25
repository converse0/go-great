package com.masuta.gogreat.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.handlers.CreateUserParams
import com.masuta.gogreat.domain.handlers.GetUserParams
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val createUserParams: CreateUserParams,
    private val getUserParams: GetUserParams,
    private val repository: ProfileRepository
) :ViewModel() {

    var errorMessage by mutableStateOf("")

    fun setParameters(
        age: Int?,
        weight: Int,
        height: Int,
        desiredWeight: Int,
        timesEat: Int
    ) {
        if (age != null) {
            val parametersUser = ParametersUserSet(
                age = age,
                weight = weight, height = height,
                desiredWeight = desiredWeight, eat = timesEat, gender = 1
            )
            viewModelScope.launch {
                val resp = createUserParams(parametersUser)
                println(resp)
            }
        }
    }
    fun getParameters(
        username: MutableState<String>,
        timesEat: MutableState<String>,
        age: MutableState<String>,
        weight: MutableState<String>,
        height: MutableState<String>,
        desiredWeight: MutableState<String>,
        gender: MutableState<Int>,
        diet: MutableState<Float>,
        activity: MutableState<Float>,
        routeTo: (navController: NavHostController, route: String) -> Unit,
        navController: NavHostController,
        fail: MutableState<Boolean>,
        uid: MutableState<String>
    ) {
        viewModelScope.launch {
            val resp = getUserParams()
            if (resp.data!= null) {

                println(resp.data.age)
                println(resp.data.username)
                username.value = resp.data.username
                age.value = resp.data.age.toString()
                timesEat.value = resp.data.eat.toString()
                weight.value = resp.data.weight.toString()
                height.value = resp.data.height.toString()
                desiredWeight.value = resp.data.desiredWeight.toString()
                gender.value = resp.data.gender
                diet.value = UserDiet.valueOf(resp.data.diet.uppercase()).value.toFloat()
                activity.value = UserActivity.valueOf(resp.data.activity.uppercase()).value.toFloat()
                println("Activity: ${UserActivity.valueOf(resp.data.activity.uppercase()).value}")
                resp.data.uid?.let { uid.value = it }
            } else if (resp.code!=null) {
                resp.message?.let { errorMessage = it }
                fail.value = true
                when(resp.code) {
                    16 -> routeTo(navController, "sign_in")
                    2, 5, 13 -> routeTo(navController, "about")
                }
            }

        }
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
        age: Int,
        weight: Int,
        height: Int,
        desiredWeight: Int,
        timesEat: Int,
        activity: Float,
        diet: Float,
        gender: Int,
        uid: String
    ): String {
        val params = ParametersUserSet (
            age = age,
            weight = weight,
            height = height,
            desiredWeight = desiredWeight,
            eat = timesEat,
            activity = activity.toInt(),
            diet = diet.toInt(),
            gender = gender,
            uid=uid
        )
        val resp = repository.updateParameters(params)
        println("Response: $resp")
        return resp
    }

    fun uploadImage(im: ImageBitmap) {
        viewModelScope.launch {
            val resp = repository.uploadImage(im)
            println("Response: $resp")
        }
    }
}