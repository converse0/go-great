package com.masuta.gogreat.presentation.profile

import android.widget.Toast
import androidx.compose.runtime.MutableState
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
        fail: MutableState<Boolean>,
        uid: MutableState<String>
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
                resp.uid?.let {
                    uid.value = it
                }
            } else if (message!=null
                &&message.isNotEmpty()) {
                fail.value = true
                routeTo(navController, "sign-in")
            }

//            else {
//                fail.value = true
//                routeTo(navController, "about")
//            }
        }
}

    fun getParameters(gender: MutableState<Int>) {
        viewModelScope.launch {
            val (resp, message) = getUserParams()

           val respInt = when {
               resp!=null -> null
               message!!.isNotEmpty()-> -6
            //   message.isNotEmpty() &&
              //         !message.contains("Authentication failed") -> 6
               else -> null
           }
            if (respInt==null) {
                if (resp != null) {
                    gender.value=resp.gender
                }
            } else {
                gender.value = respInt
            }
        }
    }

    suspend fun updateParams(
        age: Int,
        weight: Int,
        height: Int,
        desiredWeight: Int,
        timesEat: Int,
        activity: Int,
        diet: Int,
        gender: Int,
        uid: String
    ): String {
        val params = ParametersUserSet (
            age = age,
            weight = weight,
            height = height,
            desiredWeight = desiredWeight,
            eat = timesEat,
            activity = activity,
            diet = diet,
            gender = gender,
            uid=uid
        )
        val resp = repository.updateParameters(params)
        println("Response: $resp")
        return resp
    }
}