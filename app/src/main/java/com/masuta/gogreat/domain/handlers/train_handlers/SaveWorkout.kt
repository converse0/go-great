package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.domain.model.Training

class SaveWorkout(
    private val trainService: TrainService
) {

    suspend operator fun invoke(newTrain: Training) {
        return trainService.saveTraining(newTrain)
    }

}