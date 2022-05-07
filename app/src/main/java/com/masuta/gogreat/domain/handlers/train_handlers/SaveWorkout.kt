package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository

class SaveWorkout(
    private val repository: TrainRepository
) {

    suspend operator fun invoke(newTrain: Training) {
        repository.save(newTrain)
        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true
    }

}