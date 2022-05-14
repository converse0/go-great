package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.domain.model.TrainingExercise

class SaveLocalExercise(
    private val trainService: TrainService
) {

    suspend operator fun invoke(exercise: TrainingExercise) {
        trainService.saveLocalExercise(exercise)
    }

}