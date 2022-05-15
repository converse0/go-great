package com.masuta.gogreat.core.model

import com.masuta.gogreat.utils.validate

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
) {
    fun validation(): TrainingExercise {
        return this.copy(
            duration = duration.validate() + "s",
            relax = relax.validate() + "s"
        )
    }
}

enum class ExerciseType(val value: Int) {
    ARMS(0),
    LEGS(1),
    PRESS(2),
    BACK(3),
    CHEST(4),
    SHOULDER(5),
    TRICEPS(6),
    BICEPS(7),
    OTHER(8)
}

@kotlinx.serialization.Serializable
data class ExerciseResponse(
    val data: List<TrainingExercise>? = null,
    val message: String? = null,
    val code: Int? = null,
    val status: Boolean? = null
)
