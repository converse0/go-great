package com.masuta.gogreat.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.data.repository.ResponseParams
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.ParametersUserSet

interface ProfileRepository {

    var isLoadData: Boolean

    suspend fun createParameters(params: ParametersUserSet): String

    suspend fun getParameters(): ResponseParams

    suspend fun updateParameters(params: ParametersUserSet): String

    suspend fun uploadImage(image: ImageBitmap): String

    suspend fun getLocalProfileParams(): ParametersUser?
    suspend fun setLocalProfileParams(params: ParametersUser)
}