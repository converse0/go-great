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
    val clearExerciseData: ClearExerciseData,
    val saveExercise: SaveExercise,
    val getAllExercise: GetAllExercise,
    val getTrainingByUid: GetTrainingByUid,
    val setCurrentExerciseAndSets: SetCurrentExerciseAndSets,
    val errorHandler: ErrorHandler
)
