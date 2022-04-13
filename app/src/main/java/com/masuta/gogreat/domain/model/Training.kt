package com.masuta.gogreat.domain.model
@kotlinx.serialization.Serializable
data class Training(
    val exercises: List<TrainingExercise>,
    val interval: String,
    val name: String,
    val uid: String? = null,
    var image: String? = null
)

@kotlinx.serialization.Serializable
data class TrainingResponse(
    val data: List<Training>? = null,
    val message: String? = null,
    val status: Boolean? = null
)
@kotlinx.serialization.Serializable
data class TrainingDetailsResponse(
    val data: Training? = null,
    val message: String? = null,
    val status: Boolean? = null
)