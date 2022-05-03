package com.masuta.gogreat.presentation.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.handlers.GetUserParams
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.domain.repository.ProfileRepository
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserParams: GetUserParams,
    private val repository: ProfileRepository
) :ViewModel() {

    var errorMessage by mutableStateOf("")

    var isUploadImage = mutableStateOf(true)

    var userParams = mutableStateOf<ParametersUser?>(null)

    var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    fun getParameters(
        fail: MutableState<Boolean>,
        navController: NavHostController,
        context: Context,
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
                } else if (resp.code != null) {
                    resp.message?.let { errorMessage = it }
                    fail.value = true
                    when(val error = handleErrors(resp.code)) {
                        is Timeout -> {
                            Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            routeTo(navController, error.errRoute)
                        }
                    }

//                    when(resp.code) {
//                        16 -> routeTo(navController, "sign-in")
//                        2, 5, 13 -> routeTo(navController, "about")
//                    }
                }
            }
        } else {
            viewModelScope.launch {
                repository.getLocalProfileParams()?.let {
                    userParams.value = it
                }
            }
        }
    }

    fun getParameters(gender: MutableState<Int>) {
        viewModelScope.launch {
            val resp = getUserParams()
            println("resp getUserParams: $resp")
            resp.data?.let {
                gender.value = it.gender
                val params = ParametersUser().copy(image = it.image)
                repository.setLocalProfileParams(params)
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

    suspend fun getUserParameters():Boolean {
        val resp = getUserParams()
        resp.data?.let {
            val params = ParametersUser().copy(image = it.image)
            repository.setLocalProfileParams(params)
            return true
        }?: resp.code?.let {
            return false
        }
            return false
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

    suspend fun uploadImage(im: ImageBitmap): Pair<String?,String?> {
        isDataLoad = true
        val resp = repository.uploadImage(im)
        isUploadImage.value = false

        resp.data?.let {
            return Pair(null, it)
        } ?: resp.message?.let {
            return Pair(it, null)
        } ?: resp.code?.let {
            val respErr= when(it) {
                16 -> "Access Token is expired"
                5,2 -> "profile is not found"
                else -> "Request Time out exceeded"
            }

            return Pair(respErr, null)
        }

        return Pair(null, null)

    }
}