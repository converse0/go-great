package com.masuta.gogreat.core.handlers.train_handlers

import com.masuta.gogreat.core.handlers.ErrorHandler

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
    val startTraining: StartTraining,
    val clearLocalExerciseData: ClearLocalExerciseData,
    val saveLocalExercise: SaveLocalExercise,
    val getAllLocalExercise: GetAllLocalExercise,
    val getLocalTrainingByUid: GetLocalTrainingByUid,
    val setLocalExerciseAndSets: SetLocalExerciseAndSets,
    val errorHandler: ErrorHandler
)
