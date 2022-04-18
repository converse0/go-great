package com.masuta.gogreat.domain.model
@kotlinx.serialization.Serializable
data class Training(
    var exercises: List<TrainingExercise>,
    val interval: String,
    var name: String,
    var uid: String? = null,
    var image: String? = null
) {
    fun validateExerciseData(): Training {
        return this.copy(
            exercises = exercises.map { it.validation() }
        )
    }
}

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

@kotlinx.serialization.Serializable
data class TrainingExerciseUpdate(
    val exercises: List<TrainingExercise>,
    val uid: String? = null
)