package com.masuta.gogreat.domain.handlers.profile_handlers

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.domain.model.ResponseParamsIm
import com.masuta.gogreat.domain.repository.ProfileRepository

class UploadImage(
    private val repository: ProfileRepository
) {

    private var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    suspend operator fun invoke(im: ImageBitmap): ResponseParamsIm {
        isDataLoad = true
        return repository.uploadImage(im)
    }
}