package com.masuta.gogreat.core.providers

import com.masuta.gogreat.core.model.ExerciseResponse
import com.masuta.gogreat.core.model.Training
import com.masuta.gogreat.core.model.TrainingExercise
import com.masuta.gogreat.core.model.TrainingResponse

interface TrainRepository {
    suspend fun findAll(): TrainingResponse
    suspend fun findById(id: Long): ExerciseResponse
    suspend fun save(newTrain: Training)
    suspend fun getPassTrainings(): TrainingResponse
    suspend fun getMyTrainings(): TrainingResponse
    suspend fun getTrainingDetail(uid: String): Training?
    suspend fun getCurrentTraining(): TrainingResponse
    suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>)
    suspend fun startTraining(uid: String)
    suspend fun finishTraining(uid: String)
}