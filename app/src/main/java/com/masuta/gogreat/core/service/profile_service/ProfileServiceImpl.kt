package com.masuta.gogreat.core.service.profile_service

import androidx.compose.ui.graphics.ImageBitmap
import com.masuta.gogreat.core.store.ProfileStore
import com.masuta.gogreat.core.model.*
import com.masuta.gogreat.core.providers.Profile

data class GetParametersResponse(
    val data: ParametersUser? = null,
    val code: Int? = null,
    val message: String? = null
)

class ProfileServiceImpl(
    private val profile: Profile,
    private val store: ProfileStore
): ProfileService {

    override suspend fun getParameters(): GetParametersResponse {
        val localProfileParams = store.getLocalProfileParams()

        if (localProfileParams == null && userToken != null) {
            val resp = profile.getParameters()

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
                return GetParametersResponse(params)

            } else if (resp.code != null) {
                return GetParametersResponse(code = resp.code, message = resp.message)
            }
        }

        return GetParametersResponse(localProfileParams)
    }

    override suspend fun createParameters(parametersUser: ParametersUserSet) {
        profile.createParameters(parametersUser)
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
        store.setLocalProfileParams(null)

        var resp = UpdateParamsResponse()
        if (userToken != null) {
            resp = profile.updateParameters(params)
        }

        return resp
    }

    override suspend fun uploadImage(im: ImageBitmap): ResponseParamsIm {
        store.setLocalProfileParams(null)
        return profile.uploadImage(im)
    }

}