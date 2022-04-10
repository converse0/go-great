package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.repository.ProfileRepository

class CreateUserParams(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(params: ParametersUserSet): String {
        return repository.createParameters(params)
    }
}