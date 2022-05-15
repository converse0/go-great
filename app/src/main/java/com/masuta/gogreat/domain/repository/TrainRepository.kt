package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.domain.model.ExerciseResponse
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.TrainingResponse

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