package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.domain.model.ExerciseResponse

class GetExercisesById(
    private val trainService: TrainService
) {

    suspend operator fun invoke(id: Long): ExerciseResponse {
        return trainService.getExercisesById(id)
    }

}