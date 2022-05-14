package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.repository.TrainRepository

class StartTraining(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    suspend operator fun invoke(uid: String) {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        repository.startTraining(uid)
        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true
    }

}