package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.service.train_service.TrainService

data class LocalExerciseAndSets(
    val indexExercise: Int,
    val sets: Int
)

class SetCurrentExerciseAndSets(
    private val trainService: TrainService
) {

    suspend operator fun invoke(indexExercise: Int, sets: Int) {
        val exerciseAndSets = LocalExerciseAndSets(indexExercise, sets)
        trainService.setCurrentExerciseAndSets(exerciseAndSets)
    }

}