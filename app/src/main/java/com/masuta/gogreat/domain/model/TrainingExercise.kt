package com.masuta.gogreat.domain.model

@kotlinx.serialization.Serializable
data class TrainingExercise(
    val count: Int,
    val duration: String,
    val numberOfSets: Int,
    val numberOfRepetitions: Int,
    val name: String,
    val relax: String,
    val type: String,
    val uid: String,
    val image: String = "",
    val video: String = "",
    val description: String = "",
    val technique: String = "",
    val mistake: String = ""
)

enum class ExerciseType(val value: Int) {
    ARMS(0),
    LEGS(1),
    OTHER(2),
}

@kotlinx.serialization.Serializable
data class ExerciseResponse(
    val data: List<TrainingExercise>? = null,
    val message: String? = null,
    val status: Boolean? = null
)
