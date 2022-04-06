package com.masuta.gogreat.domain.model
import kotlinx.serialization.*


@Serializable
data class User(
//    val id: Int? = null,
//    val name: String?=null,
    val email: String,
    val password: String,
//    val createdAt: String?=null,
//    val updatedAt: String?=null
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

var userToken: String? = null
var refreshUserToken: String? = null