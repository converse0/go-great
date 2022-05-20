package com.masuta.gogreat.core.handlers.profile_handlers

import com.masuta.gogreat.core.service.profile_service.GetParametersResponse
import com.masuta.gogreat.core.service.profile_service.ProfileService

class GetParameters(
    private val profileService: ProfileService
) {

    suspend operator fun invoke(): GetParametersResponse {
        return profileService.getParameters()
    }
}