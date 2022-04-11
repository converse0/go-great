package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.domain.model.ExerciseResponse
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingResponse

interface TrainRepository {
    suspend fun findAll(): TrainingResponse
    suspend fun findById(id: Long): ExerciseResponse
    suspend fun save(newTrain: Training)
    fun delete(newTrain: Training)
}