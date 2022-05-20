package com.masuta.gogreat.core.service.auth_service

import kotlinx.serialization.Serializable

@Serializable
data class UserSignupRequest(
    val username: String,
    val email: String,
    val password: String,
)

@Serializable
data class UserSigninRequest(
    val email: String,
    val password: String,
)