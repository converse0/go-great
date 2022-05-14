package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.model.TrainResponse
import com.masuta.gogreat.domain.repository.TrainRepository
import kotlin.system.measureTimeMillis

class GetPastWorkouts(
    private val trainService: TrainService
) {

    suspend operator fun invoke(): TrainResponse {
        return trainService.getPastWorkouts()
    }

}