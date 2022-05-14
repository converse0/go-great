package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository

class SetExerciseParameters(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    suspend operator fun invoke(
        uid: String,
        listExercises: List<TrainingExercise>,
        indexExercise: Int,
        exerciseSets: Int
    ) {

        repository.setExerciseParams(uid, listExercises)
        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true

        store.setLocalCurrentExercise(indexExercise)
        store.setLocalCurrentExerciseSets(exerciseSets)

        val training = store.getLocalTrainingByUid(uid).let {
            it?.copy(
                exercises = listExercises
            )
        }
        training?.let {
            store.saveLocalTraining(it)
        }

    }

}