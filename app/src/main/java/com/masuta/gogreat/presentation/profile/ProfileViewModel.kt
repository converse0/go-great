package com.masuta.gogreat.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.use_case.ProfileUseCase
import dagger.hilt.android.scopes.ViewModelScoped
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
            val parametersUser = ParametersUser(
                age = age,
                weight = weight, height = height,
                desiredWeight = desiredWeight, eat = timesEat, gender = 1
            )
            val pUseCase = ProfileUseCase()
            viewModelScope.launch {
                val resp = pUseCase.updateParameters(parametersUser)
                println(resp)
            }
        }
    }
}