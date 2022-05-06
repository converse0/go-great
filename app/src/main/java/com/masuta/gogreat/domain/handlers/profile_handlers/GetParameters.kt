package com.masuta.gogreat.domain.handlers.profile_handlers

import android.content.Context
import android.widget.Toast
import com.masuta.gogreat.data.store.ProfileStore
import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.UserActivity
import com.masuta.gogreat.domain.model.UserDiet
import com.masuta.gogreat.domain.repository.ProfileRepository
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors

data class GetParametersResponse(
    val data: ParametersUser? = null,
    val code: Int? = null,
    val message: String? = null
)

class GetParameters(
    private val repository: ProfileRepository,
    private val store: ProfileStore,
) {

    private var isDataLoad: Boolean = true
        get() = repository.isLoadData
        set(value) {
            field = value
            repository.isLoadData = value
        }

    suspend operator fun invoke(): GetParametersResponse {
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
}