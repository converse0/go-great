package com.masuta.gogreat.data.store

import com.masuta.gogreat.domain.model.Training

interface TrainStore {

    suspend fun getLocalCurrentExercise(): Int?
    suspend fun setLocalCurrentExercise(indexExercise: Int?)

    suspend fun getLocalCurrentExerciseSets(): Int?
    suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?)

    suspend fun getLocalWorkouts(): List<Training>
    suspend fun setLocalWorkouts(workouts: List<Training>)

    suspend fun getLocalCurrentWorkout(): Training?
    suspend fun setLocalCurrentWorkout(workout: Training?)

    suspend fun getLocalPastWorkouts(): List<Training>
    suspend fun setLocalPastWorkouts(workouts: List<Training>)

}