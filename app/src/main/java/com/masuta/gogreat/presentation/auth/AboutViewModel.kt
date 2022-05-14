package com.masuta.gogreat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.handlers.profile_handlers.CreateParameters
import com.masuta.gogreat.domain.handlers.profile_handlers.ProfileHandlers
import com.masuta.gogreat.domain.model.ParametersUserSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val profileHandlers: ProfileHandlers
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
        viewModelScope.launch {
            profileHandlers.createParameters(
                age = age,
                gender = gender,
                weight = weight,
                height = height,
                desiredWeight = desiredWeight,
                timesEat = timesEat,
                activity = activity,
                diet = diet
            )
        }
    }
}