package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.repository.TrainRepository

class EndTraining(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    suspend operator fun invoke() {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        store.setLocalCurrentWorkout(null)

        repository.currentWorkoutDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.workoutsDataReload = true
    }

}