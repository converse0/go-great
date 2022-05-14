package com.masuta.gogreat.core.service.profile_service

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.store.ProfileStore
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.ProfileRepository

data class GetParametersResponse(
    val data: ParametersUser? = null,
    val code: Int? = null,
    val message: String? = null
)

class ProfileServiceImpl(
    private val repository: ProfileRepository,
    private val store: ProfileStore
): ProfileService {

    private var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    override suspend fun getParameters(): GetParametersResponse {
        if (isDataLoad) {
            val resp = repository.getParameters()
            if (resp.data != null) {
                val params = ParametersUser(
                    username = resp.data.username,
                    activity = UserActivity.valueOf(resp.data.activity.uppercase()).value,
                    age = resp.data.age,
                    desiredWeight = resp.data.desiredWeight,
                    diet = UserDiet.valueOf(resp.data.diet.uppercase()).value,
                    eat = resp.data.eat,
                    gender = resp.data.gender,
                    height = resp.data.height,
                    weight = resp.data.weight,
                    uid = resp.data.uid,
                    image = resp.data.image
                )
                store.setLocalProfileParams(params)
                isDataLoad = false
                return GetParametersResponse(params)

            } else if (resp.code != null) {
                return GetParametersResponse(code = resp.code, message = resp.message)
            }
        } else {
            store.getLocalProfileParams()?.let {
                return GetParametersResponse(it)
            }
        }
        return GetParametersResponse()
    }

    override suspend fun createParameters(parametersUser: ParametersUserSet) {
        repository.createParameters(parametersUser)
    }

    override suspend fun updateParameters(userParams: ParametersUser): UpdateParamsResponse {
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

    override suspend fun uploadImage(im: ImageBitmap): ResponseParamsIm {
        isDataLoad = true
        return repository.uploadImage(im)
    }

}