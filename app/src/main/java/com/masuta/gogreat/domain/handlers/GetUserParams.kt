package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.domain.model.ParametersUserGet
import com.masuta.gogreat.domain.repository.ProfileRepository

class GetUserParams(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Pair<ParametersUserGet?, String?> {
        return repository.getParameters()
    }
}