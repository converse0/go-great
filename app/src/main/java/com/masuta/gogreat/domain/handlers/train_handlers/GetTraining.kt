package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.model.Training

data class GetTrainingResponse(
    val localTraining: Training?,
    val currentExercise: Int?,
    val currentExerciseSets: Int?
)

class GetTraining(
    private val store: TrainStore
) {

    suspend operator fun invoke(uid: String): GetTrainingResponse {
        val training = store.getLocalTrainingByUid(uid)
        val exerciseCurrent = store.getLocalCurrentExercise()
        val sets = store.getLocalCurrentExerciseSets()

        return GetTrainingResponse(training, exerciseCurrent, sets)
    }

}