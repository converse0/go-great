package com.masuta.gogreat.core.handlers.profile_handlers

import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.core.model.ParametersUser
import com.masuta.gogreat.core.model.UpdateParamsResponse

class UpdateParameters(
    private val profileService: ProfileService
) {

    suspend operator fun invoke(userParams: ParametersUser): UpdateParamsResponse {
        return profileService.updateParameters(userParams)
    }
}