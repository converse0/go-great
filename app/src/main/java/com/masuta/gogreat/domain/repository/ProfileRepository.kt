package com.masuta.gogreat.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.domain.model.*

interface ProfileRepository {

    var isLoadData: Boolean

    suspend fun createParameters(params: ParametersUserSet): String?

    suspend fun getParameters(): ResponseParams

    suspend fun updateParameters(params: ParametersUserSet): UpdateParamsResponse

    suspend fun uploadImage(image: ImageBitmap): ResponseParamsIm

    suspend fun getLocalProfileParams(): ParametersUser?

    suspend fun setLocalProfileParams(params: ParametersUser)
}