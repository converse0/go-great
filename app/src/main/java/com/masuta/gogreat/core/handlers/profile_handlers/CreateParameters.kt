package com.masuta.gogreat.core.handlers.profile_handlers

import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.core.model.ParametersUserSet

class CreateParameters(
     private val profileService: ProfileService
) {

    suspend operator fun invoke(
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

            profileService.createParameters(parametersUser)
        }

    }
}