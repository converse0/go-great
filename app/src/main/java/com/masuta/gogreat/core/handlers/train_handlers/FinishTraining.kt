package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService

class FinishTraining(
    private val trainService: TrainService,
) {

    suspend operator fun invoke(uid: String) {
        trainService.finishTraining(uid)
    }

}