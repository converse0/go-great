package com.masuta.gogreat.domain.model

@kotlinx.serialization.Serializable
data class ParametersUserSet(
    val activity: Int = 0,//UserActivity = UserActivity.BASIC,
    val age: Int,
    val desiredWeight: Int,
    val diet: Int = 0,//UserDiet = UserDiet.BALANCED,
    val eat: Int,
    val gender: Int,
    val height: Int,
    val weight: Int
)

@kotlinx.serialization.Serializable
data class ParametersUserGet(
    val activity: String = "Basic",//UserActivity = UserActivity.BASIC,
    val age: Int,
    val desiredWeight: Int,
    val diet: String="Balanced",//UserDiet = UserDiet.BALANCED,
    val eat: Int,
    val gender: Int,
    val height: Int,
    val weight: Int
)