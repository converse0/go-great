package com.masuta.gogreat.domain.handlers.profile_handlers

import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.repository.ProfileRepository

class CreateParameters(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(parametersUser: ParametersUserSet) {
        repository.createParameters(parametersUser)
    }
}