package com.masuta.gogreat.domain.model

data class ParametersUser(
    val activity: UserActivity = UserActivity.BASIC,
    val age: Int,
    val desiredWeight: Int,
    val diet: UserDiet = UserDiet.BALANCED,
    val eat: Int,
    val gender: Int,
    val height: Int,
    val weight: Int
)