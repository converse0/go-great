package com.masuta.gogreat.presentation.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.core.handlers.profile_handlers.ProfileHandlers
import com.masuta.gogreat.core.model.ParametersUser
import com.masuta.gogreat.core.model.gender
import com.masuta.gogreat.presentation.setSex
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

    private var isDataLoad = true

    var isUploadImage = mutableStateOf(true)

    var userParams = mutableStateOf<ParametersUser?>(null)

    fun getParameters(
        navController: NavHostController,
    ) {
        if (isDataLoad) {
            isDataLoad = false
            viewModelScope.launch {
                val resp = profileHandlers.getParameters()
                resp.data?.let {
                    userParams.value = it
                } ?: resp.code?.let { code ->
                    profileHandlers.errorHandler(code, resp.message, navController)
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
            println("GET PARAMETERS GENDER: $resp")

            resp.data?.let {
                gender.value = it.gender
            } ?: resp.code?.let {
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

    fun updateParams(
        navController: NavHostController,
        context: Context,
        userParams: ParametersUser,
        lazyListState: LazyListState
    ) {
       viewModelScope.launch {
           val resp = profileHandlers.updateParameters(userParams)
           resp.code?.let { code ->
               profileHandlers.errorHandler(code, resp.message, navController)
           } ?: withContext(Dispatchers.Main) {
               gender = userParams.gender
               setSex(context = context, gender = userParams.gender)
                   Toast.makeText(
                       context,
                       "Update user parameters Success",
                       Toast.LENGTH_SHORT
                   ).show()
               lazyListState.scrollToItem(0)
           }
       }
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