package com.masuta.gogreat.domain.handlers.profile_handlers

import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.UpdateParamsResponse

class UpdateParameters(
    private val profileService: ProfileService
) {

    suspend operator fun invoke(userParams: ParametersUser): UpdateParamsResponse {
        return profileService.updateParameters(userParams)
    }
}