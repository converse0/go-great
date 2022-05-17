package com.masuta.gogreat.core.model

@kotlinx.serialization.Serializable
data class ParametersUserSet(
    val activity: Int = 0,
    val age: Int,
    val desiredWeight: Int,
    val diet: Int = 0,
    val eat: Int,
    val gender: Int,
    val height: Int,
    val weight: Int,
    val uid: String? = null,
    var image: ByteArray? = null
)

@kotlinx.serialization.Serializable
data class ParametersUserGet(
    val username: String,
    val activity: String = "Basic",
    val age: Int,
    val desiredWeight: Int,
    val diet: String = "Balanced",
    val eat: Int,
    val gender: Int,
    val height: Int,
    val weight: Int,
    val uid: String? = null,
    var image: String? = null
)

data class ParametersUser(
    var username: String = "",
    var activity: Int = 0,
    var age: Int = 0,
    var desiredWeight: Int = 0,
    var diet: Int = 0,
    var eat: Int = 0,
    var gender: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val uid: String? = null,
    var image: String? = null
)