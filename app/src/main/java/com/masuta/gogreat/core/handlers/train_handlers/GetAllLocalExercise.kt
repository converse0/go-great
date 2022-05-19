package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.TrainingExercise

class GetAllLocalExercise(
    private val trainService: TrainService
) {

    suspend operator fun invoke(): List<TrainingExercise> {
        return trainService.getAllExercise()
    }
}