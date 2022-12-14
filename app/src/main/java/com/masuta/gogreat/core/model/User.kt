package com.masuta.gogreat.core.model

import kotlinx.serialization.*

@Serializable
data class User(
    val username: String? = null,
    val email: String,
    val password: String,
    val passwordConfirm: String? = null
)

@Serializable
data class LoginResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val username: String
)
@Serializable
data class Response(
    val message: String,
    val status: Boolean,
    val data: LoginResponse? = null
)

@Serializable
data class UpdateParamsResponse(
    val message: String? = null,
    val status: Boolean? = null,
    val code: Int? = null
)

var userToken: String? = null
var refreshUserToken: String? = null
var gender: Int? = null