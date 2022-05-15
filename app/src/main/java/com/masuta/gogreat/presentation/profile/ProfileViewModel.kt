package com.masuta.gogreat.presentation.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.core.handlers.profile_handlers.ProfileHandlers
import com.masuta.gogreat.core.model.ParametersUser
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileHandlers: ProfileHandlers,
    private val authHandlers: AuthHandlers,
) :ViewModel() {

    var errorMessage by mutableStateOf("")

    var isUploadImage = mutableStateOf(true)

    var userParams = mutableStateOf<ParametersUser?>(null)

    fun getParameters(
        fail: MutableState<Boolean>,
        navController: NavHostController,
        context: Context,
    ) {
        viewModelScope.launch {
            val resp = profileHandlers.getParameters()
            resp.data?.let {
                userParams.value = it
            } ?: resp.code?.let {
                resp.message?.let { errorMessage = it }
                fail.value = true
                when(val error = handleErrors(resp.code)) {
                    is Timeout -> {
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            routeTo(navController, error.errRoute)
                            Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun getTokens() {
        viewModelScope.launch {
            authHandlers.getToken()
        }
    }

    fun getParameters(gender: MutableState<Int>) {
        viewModelScope.launch {
            val resp = profileHandlers.getParameters()
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

    suspend fun getUserParameters(): Boolean {
        val resp = profileHandlers.getParameters()
        resp.data?.let {
            return true
        } ?: resp.code?.let {
            return false
        }
            return false
    }

    suspend fun updateParams(
        context: Context,
        navController: NavHostController,
        userParams: ParametersUser,
    ): String? {
        val resp = profileHandlers.updateParameters(userParams)
        resp.code?.let {
            when(val error = handleErrors(it)) {
                is Timeout -> {
                    Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        routeTo(navController, error.errRoute)
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return resp.message
    }

    suspend fun uploadImage(im: ImageBitmap): Pair<String?,String?> {
        val resp = profileHandlers.uploadImage(im)
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