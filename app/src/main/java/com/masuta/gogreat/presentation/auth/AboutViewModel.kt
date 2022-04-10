package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.use_case.ProfileUseCase
import kotlinx.coroutines.launch

class AboutViewModel: ViewModel() {

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
                gender = 1,
               activity = activity, diet = diet
            )
            val pUseCase = ProfileUseCase()
            viewModelScope.launch {
                val resp = pUseCase.createParameters(parametersUser)
                println(resp)
            }
        }
    }
}