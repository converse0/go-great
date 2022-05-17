package com.masuta.gogreat.core.service.train_service

import com.masuta.gogreat.core.handlers.train_handlers.LocalExerciseAndSets
import com.masuta.gogreat.core.model.*

interface TrainService {

    suspend fun getCurrentWorkout(): TrainResponse
    suspend fun getWorkouts(): TrainResponse
    suspend fun getPastWorkouts(): TrainResponse
    suspend fun saveTraining(newTrain: Training)
    suspend fun startTraining(uid: String): StartTrainingResponse
    suspend fun finishTraining(uid: String): FinishTrainingResponse
    suspend fun endTraining()

    suspend fun getLocalTrainings(uid: String): GetTrainingResponse
    suspend fun getExercisesById(id: Long): ExerciseResponse
    suspend fun setExerciseParameters(params: SetExerciseParamsRequest): SetExerciseParamsResponse
    suspend fun clearLocalExerciseData()
    suspend fun saveLocalExercise(exercise: TrainingExercise)
    suspend fun getAllLocalExercise(): List<TrainingExercise>
    suspend fun getLocalTrainingByUid(uid: String): Training?
    suspend fun setLocalExerciseAndSets(exerciseAndSets: LocalExerciseAndSets)

}