package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.TrainResponse

class GetCurrentWorkout(
    private val trainService: TrainService
) {

    suspend operator fun invoke(): TrainResponse {
        return trainService.getCurrentWorkout()
    }
}