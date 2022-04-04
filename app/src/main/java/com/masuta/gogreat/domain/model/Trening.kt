package com.masuta.gogreat.domain.model

data class Trening(
    val exercises: List<TrainingExercise>,
    val interval: String,
    val name: String
)