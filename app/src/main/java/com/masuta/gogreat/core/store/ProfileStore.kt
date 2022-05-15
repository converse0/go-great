package com.masuta.gogreat.core.store

import com.masuta.gogreat.core.model.ParametersUser

interface ProfileStore {

    suspend fun getLocalProfileParams(): ParametersUser?

    suspend fun setLocalProfileParams(params: ParametersUser?)
}