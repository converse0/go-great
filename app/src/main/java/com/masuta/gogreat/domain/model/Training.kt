package com.masuta.gogreat.domain.model
@kotlinx.serialization.Serializable
data class Training(
    val exercises: List<TrainingExercise>,
    val interval: String,
    val name: String
)

@kotlinx.serialization.Serializable
data class TrainingResponse(
    val data: List<Training>?,
    val message: String?,
    val status: Boolean?
)