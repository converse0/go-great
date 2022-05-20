package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.ExerciseResponse

class GetExercisesById(
    private val trainService: TrainService
) {

    suspend operator fun invoke(id: Long): ExerciseResponse {
        return trainService.getExercisesById(id)
    }

}