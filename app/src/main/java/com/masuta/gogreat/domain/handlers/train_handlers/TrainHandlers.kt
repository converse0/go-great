package com.masuta.gogreat.domain.handlers.train_handlers

data class TrainHandlers(
    val endTraining: EndTraining,
    val finishTraining: FinishTraining,
    val getCurrentWorkout: GetCurrentWorkout,
    val getExercisesById: GetExercisesById,
    val getPastWorkouts: GetPastWorkouts,
    val getTraining: GetTraining,
    val getWorkouts: GetWorkouts,
    val saveWorkout: SaveWorkout,
    val setExerciseParameters: SetExerciseParameters,
    val startTraining: StartTraining
)
