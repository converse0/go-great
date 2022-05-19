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

    suspend fun getTrainings(uid: String): GetTrainingResponse
    suspend fun getExercisesById(id: Long): ExerciseResponse
    suspend fun setExerciseParameters(params: SetExerciseParamsRequest): SetExerciseParamsResponse
    suspend fun clearExerciseData()
    suspend fun saveExercise(exercise: TrainingExercise)
    suspend fun getAllExercise(): List<TrainingExercise>
    suspend fun getTrainingByUid(uid: String): Training?
    suspend fun setCurrentExerciseAndSets(exerciseAndSets: LocalExerciseAndSets)

}