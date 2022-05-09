package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.domain.model.ExerciseResponse
import com.masuta.gogreat.domain.repository.TrainRepository

class GetExercisesById(
    private val repository: TrainRepository
) {

    suspend operator fun invoke(id: Long): ExerciseResponse {
        return repository.findById(id)
    }

}