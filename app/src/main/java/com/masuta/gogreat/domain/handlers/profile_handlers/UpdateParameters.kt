package com.masuta.gogreat.domain.handlers.profile_handlers

import com.masuta.gogreat.data.store.ProfileStore
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.UpdateParamsResponse
import com.masuta.gogreat.domain.repository.ProfileRepository

class UpdateParameters(
    private val repository: ProfileRepository,
    private val store: ProfileStore
) {

    private var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    suspend operator fun invoke(userParams: ParametersUser): UpdateParamsResponse {
        val params = ParametersUserSet (
            age = userParams.age,
            weight = userParams.weight,
            height = userParams.height,
            desiredWeight = userParams.desiredWeight,
            eat = userParams.eat,
            activity = userParams.activity,
            diet = userParams.diet,
            gender = userParams.gender,
            uid = userParams.uid
        )
        store.setLocalProfileParams(userParams)
        val resp = repository.updateParameters(params)
        isDataLoad = true

        return resp
    }
}