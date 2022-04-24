package com.masuta.gogreat.domain.repository

import com.masuta.gogreat.domain.model.ExerciseResponse
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.model.TrainingResponse

interface TrainRepository {
    var workoutsDataReload: Boolean
    var pastWorkoutsDataReload: Boolean
    var currentWorkoutDataReload: Boolean

    suspend fun findAll(): TrainingResponse
    suspend fun findById(id: Long): ExerciseResponse
    suspend fun save(newTrain: Training)
    suspend fun saveLocal(newTrain: Training): String
    suspend fun saveLocalEx(ex: TrainingExercise):Int
    suspend fun getLocalEx(id: Int):TrainingExercise?
    suspend fun getAllLocalTrainings(): List<Training>?
    suspend fun getPassTrainings(): List<Training>?
    suspend fun getMyTrainings(): List<Training>?

    suspend fun getLocalWorkouts(): List<Training>
    suspend fun setLocalWorkouts(workouts: List<Training>)

    suspend fun getLocalCurrentWorkout(): Training?
    suspend fun setLocalCurrentWorkout(workout: Training)

    suspend fun getLocalPastWorkouts(): List<Training>
    suspend fun setLocalPastWorkouts(workouts: List<Training>)

    suspend fun getLocalTrainingByUid(uid: String): Training?
    suspend fun getAllLocalEx(): List<TrainingExercise>
    suspend fun clearLocalExerciseData()
    suspend fun clearLocalTrainingData()
    suspend fun getTrainingDetail(uid: String): Training
    suspend fun getCurrentTraining(): Training?
    suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>)
    suspend fun startTraining(uid: String)
    suspend fun finishTraining(uid: String)

    fun delete(newTrain: Training)
}