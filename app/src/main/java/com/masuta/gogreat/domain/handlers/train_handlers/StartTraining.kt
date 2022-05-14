package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.repository.TrainRepository

class StartTraining(
    private val trainService: TrainService
) {

    suspend operator fun invoke(uid: String) {
        trainService.startTraining(uid)
    }

}