package com.masuta.gogreat.core.providers

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.model.*

interface ProfileRepository {

    suspend fun createParameters(params: ParametersUserSet): String?

    suspend fun getParameters(): ResponseParams

    suspend fun updateParameters(params: ParametersUserSet): UpdateParamsResponse

    suspend fun uploadImage(image: ImageBitmap): ResponseParamsIm

}