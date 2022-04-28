package com.masuta.gogreat.domain.handlers

import com.masuta.gogreat.data.repository.ResponseParams
import com.masuta.gogreat.domain.repository.ProfileRepository

class GetUserParams(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): ResponseParams {
        return repository.getParameters()
    }
}