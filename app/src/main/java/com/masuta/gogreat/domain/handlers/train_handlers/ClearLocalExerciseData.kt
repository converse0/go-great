package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService

class ClearLocalExerciseData(
    private val trainService: TrainService
) {

    suspend operator fun invoke() {
        trainService.clearLocalExerciseData()
    }
}