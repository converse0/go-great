package com.masuta.gogreat.core.store

import androidx.compose.runtime.mutableStateOf
import com.masuta.gogreat.domain.model.ParametersUser

class ProfileStoreImpl: ProfileStore {

    private var profileParams = mutableStateOf<ParametersUser?>(null)


    override suspend fun getLocalProfileParams(): ParametersUser? {
        return profileParams.value
    }

    override suspend fun setLocalProfileParams(params: ParametersUser?) {
        profileParams.value = params
    }
}