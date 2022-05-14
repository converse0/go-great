package com.masuta.gogreat.domain.handlers.profile_handlers

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.domain.model.ResponseParamsIm

class UploadImage(
    private val profileService: ProfileService
) {

    suspend operator fun invoke(im: ImageBitmap): ResponseParamsIm {
        return profileService.uploadImage(im)
    }

}