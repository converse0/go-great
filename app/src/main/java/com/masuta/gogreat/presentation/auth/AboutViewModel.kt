package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.handlers.profile_handlers.CreateParameters
import com.masuta.gogreat.domain.model.ParametersUserSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val createParameters: CreateParameters
) : ViewModel() {

    fun setParameters(
        age: Int?,
        weight: Int,
        height: Int,
        desiredWeight: Int,
        timesEat: Int,
        activity: Int,
        diet: Int,
        gender: Int
    ) {
        if (age != null) {
            val parametersUser = ParametersUserSet(
                age = age,
                weight = weight, height = height,
                desiredWeight = desiredWeight, eat = timesEat,
                gender = gender,
               activity = activity, diet = diet
            )
            viewModelScope.launch {
                createParameters(parametersUser)
            }
        }
    }
}