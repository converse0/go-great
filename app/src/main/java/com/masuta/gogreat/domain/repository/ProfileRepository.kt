package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.data.repository.ResponseParams
import com.masuta.gogreat.domain.model.ParametersUserGet
import com.masuta.gogreat.domain.model.ParametersUserSet

interface ProfileRepository {

    suspend fun createParameters(params: ParametersUserSet): String

    suspend fun getParameters(): ResponseParams

    suspend fun updateParameters(params: ParametersUserSet): String

}