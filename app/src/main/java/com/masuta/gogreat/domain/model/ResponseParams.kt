package com.masuta.gogreat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseParams(
    val message: String? = null,
    val status: Boolean? = null,
    val code: Int? = null,
    val data: ParametersUserGet? = null
)

@Serializable
data class ResponseParamsIm(
    val message: String? = null,
    val status: Boolean? = null,
    val code: Int? = null,
    val data: String? = null
)