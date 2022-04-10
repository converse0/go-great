package com.masuta.gogreat.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.domain.use_case.ProfileUseCase
import kotlinx.coroutines.launch

class ProfileViewModel :ViewModel() {



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
            val pUseCase = ProfileUseCase()
            viewModelScope.launch {
                val resp = pUseCase.createParameters(parametersUser)
                println(resp)
            }
        }
    }
    fun getParameters(
        timesEat: MutableState<String>,
        age: MutableState<String>,
        weight: MutableState<String>,
        height: MutableState<String>,
        desiredWeight: MutableState<String>,
        gender: MutableState<Int>,
        diet: MutableState<Int>,
        activity: MutableState<Int>,
        routeTo: (navController: NavHostController, route: String) -> Unit,
        navController: NavHostController,
        fail: MutableState<Boolean>
    ) {
        val pUseCase = ProfileUseCase()
        viewModelScope.launch {
            val (resp,  message) = pUseCase.getParameters()
            if (resp != null) {
                println(resp.age)
                age.value = resp.age.toString()
                timesEat.value = resp.eat.toString()
                weight.value = resp.weight.toString()
                height.value = resp.height.toString()
                desiredWeight.value = resp.desiredWeight.toString()
                gender.value = resp.gender
                diet.value = UserDiet.valueOf(resp.diet.uppercase()).value
                activity.value = UserActivity.valueOf(resp.activity.uppercase()).value
            } else if (message!=null&&message.isNotEmpty()&& message.contains("Authentication failed")) {
                println("null")
                fail.value = true
                routeTo(navController, "sign-in")
            } else {
                fail.value = true
                routeTo(navController, "about")
            }
        }
}
}