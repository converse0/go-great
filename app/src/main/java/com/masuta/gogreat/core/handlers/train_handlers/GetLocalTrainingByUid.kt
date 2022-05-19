package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.model.Training

class GetLocalTrainingByUid(
    private val trainService: TrainService
) {

    suspend operator fun invoke(uid: String): Training? {
        return trainService.getTrainingByUid(uid)
    }

}