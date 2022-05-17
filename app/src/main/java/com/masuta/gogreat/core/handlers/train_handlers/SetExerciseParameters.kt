package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.SetExerciseParamsRequest
import com.masuta.gogreat.core.model.SetExerciseParamsResponse
import com.masuta.gogreat.core.model.TrainingExercise

class SetExerciseParameters(
    private val trainService: TrainService
) {

    suspend operator fun invoke(
        uid: String,
        listExercises: List<TrainingExercise>,
        indexExercise: Int,
        exerciseSets: Int
    ): SetExerciseParamsResponse {
        val params = SetExerciseParamsRequest(uid, listExercises, indexExercise, exerciseSets)
        return trainService.setExerciseParameters(params)
    }

}