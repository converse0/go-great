package com.masuta.gogreat.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.handlers.CreateUserParams
import com.masuta.gogreat.domain.handlers.GetUserParams
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val createUserParams: CreateUserParams,
    private val getUserParams: GetUserParams
) :ViewModel() {



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
        diet: MutableState<Int>,
        activity: MutableState<Int>,
        routeTo: (navController: NavHostController, route: String) -> Unit,
        navController: NavHostController,
        fail: MutableState<Boolean>
    ) {
        viewModelScope.launch {
            val (resp, message) = getUserParams()
            if (resp != null) {
                println(resp.age)
                println(resp.username)
                username.value = resp.username
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