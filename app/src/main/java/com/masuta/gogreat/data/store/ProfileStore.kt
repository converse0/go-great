package com.masuta.gogreat.data.store

import com.masuta.gogreat.domain.model.ParametersUser

interface ProfileStore {

    suspend fun getLocalProfileParams(): ParametersUser?

    suspend fun setLocalProfileParams(params: ParametersUser)
}