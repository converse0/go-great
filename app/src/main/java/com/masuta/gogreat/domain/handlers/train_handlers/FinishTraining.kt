package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.domain.repository.TrainRepository

class FinishTraining(
    private val repository: TrainRepository,
) {

    suspend operator fun invoke(uid: String) {
        repository.finishTraining(uid)
    }

}