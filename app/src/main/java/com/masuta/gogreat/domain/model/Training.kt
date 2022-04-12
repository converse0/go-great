package com.masuta.gogreat.domain.model
@kotlinx.serialization.Serializable
data class Training(
    val exercises: List<TrainingExercise>,
    val interval: String,
    val name: String
)

@kotlinx.serialization.Serializable
data class TrainingResponse(
    val data: List<Training>? = null,
    val message: String? = null,
    val status: Boolean? = null
)