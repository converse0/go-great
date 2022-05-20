package com.masuta.gogreat.core.providers

import com.masuta.gogreat.core.model.*

interface Train {
    suspend fun findAll(): TrainingResponse
    suspend fun findById(id: Long): ExerciseResponse
    suspend fun save(newTrain: Training)
    suspend fun getPassTrainings(): TrainingResponse
    suspend fun getMyTrainings(): TrainingResponse
    suspend fun getTrainingDetail(uid: String): Training?
    suspend fun getCurrentTraining(): TrainingResponse
    suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>): SetExerciseParamsResponse
    suspend fun startTraining(uid: String): StartTrainingResponse
    suspend fun finishTraining(uid: String): FinishTrainingResponse
}