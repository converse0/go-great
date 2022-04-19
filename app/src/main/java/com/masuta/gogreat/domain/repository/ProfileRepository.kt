package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.domain.model.ParametersUserGet
import com.masuta.gogreat.domain.model.ParametersUserSet

interface ProfileRepository {

    suspend fun createParameters(params: ParametersUserSet): String

    suspend fun getParameters(): Pair<ParametersUserGet?, String?>

    suspend fun updateParameters(params: ParametersUserSet): String

}