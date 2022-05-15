package com.masuta.gogreat.core.handlers.profile_handlers

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.core.model.ResponseParamsIm

class UploadImage(
    private val profileService: ProfileService
) {

    suspend operator fun invoke(im: ImageBitmap): ResponseParamsIm {
        return profileService.uploadImage(im)
    }

}