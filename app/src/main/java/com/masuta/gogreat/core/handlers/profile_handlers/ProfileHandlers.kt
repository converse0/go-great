package com.masuta.gogreat.core.handlers.profile_handlers

import com.masuta.gogreat.core.handlers.ErrorHandler

data class ProfileHandlers(
    val createParameters: CreateParameters,
    val getParameters: GetParameters,
    val updateParameters: UpdateParameters,
    val uploadImage: UploadImage,
    val errorHandler: ErrorHandler
)
