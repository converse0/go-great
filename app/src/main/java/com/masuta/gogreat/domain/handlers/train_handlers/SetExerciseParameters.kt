package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.domain.model.SetExerciseParamsRequest
import com.masuta.gogreat.domain.model.TrainingExercise

class SetExerciseParameters(
    private val trainService: TrainService
) {

    suspend operator fun invoke(
        uid: String,
        listExercises: List<TrainingExercise>,
        indexExercise: Int,
        exerciseSets: Int
    ) {
        val params = SetExerciseParamsRequest(uid, listExercises, indexExercise, exerciseSets)
        trainService.setExerciseParameters(params)
    }

}