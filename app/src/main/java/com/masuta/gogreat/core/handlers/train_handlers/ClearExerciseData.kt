package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService

class ClearExerciseData(
    private val trainService: TrainService
) {

    suspend operator fun invoke() {
        trainService.clearExerciseData()
    }
}