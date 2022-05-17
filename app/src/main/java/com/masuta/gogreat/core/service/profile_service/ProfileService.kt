package com.masuta.gogreat.core.service.profile_service

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.model.*

interface ProfileService {

    suspend fun getParameters(): GetParametersResponse

    suspend fun createParameters(parametersUser: ParametersUserSet)

    suspend fun updateParameters(userParams: ParametersUser): UpdateParamsResponse

    suspend fun uploadImage(im: ImageBitmap): ResponseParamsIm

}