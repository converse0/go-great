package com.masuta.gogreat.core.model

@kotlinx.serialization.Serializable
data class Training(
    var exercises: List<TrainingExercise>,
    val interval: String,
    var name: String,
    var uid: String? = null,
    var image: String? = null,
    var date: String? = null
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
    val code: Int? = null,
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

@kotlinx.serialization.Serializable
data class StartTrainingResponse(
    val code: Int? = null,
    val message: String? = null,
    val status: Boolean? = null
)

@kotlinx.serialization.Serializable
data class FinishTrainingResponse(
    val code: Int? = null,
    val message: String? = null,
    val status: Boolean? = null
)

@kotlinx.serialization.Serializable
data class SetExerciseParamsResponse(
    val code: Int? = null,
    val message: String? = null,
    val status: Boolean? = null
)

data class TrainResponse(
    val data: List<Training>? = null,
    val code: Int? = null,
    val message: String? = null
)

data class GetTrainingResponse(
    val localTraining: Training?,
    val currentExercise: Int?,
    val currentExerciseSets: Int?
)

data class SetExerciseParamsRequest(
    val uid: String,
    val listExercises: List<TrainingExercise>,
    val indexExercise: Int,
    val exerciseSets: Int
)
