package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.GetTrainingResponse

class GetTraining(
    private val trainService: TrainService
) {

    suspend operator fun invoke(uid: String): GetTrainingResponse {
        return trainService.getLocalTrainings(uid)
    }

}