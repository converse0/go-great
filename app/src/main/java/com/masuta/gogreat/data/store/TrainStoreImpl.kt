package com.masuta.gogreat.data.store

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.masuta.gogreat.domain.model.Training
import javax.inject.Inject

class TrainStoreImpl @Inject constructor(
    private val context: Context
) : TrainStore {

    private var localWorkouts: List<Training> = mutableListOf()
    private var localCurrentWorkout = mutableStateOf<Training?>(null)
    private var localPastWorkouts: List<Training> = mutableListOf()

    private val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    override suspend fun getLocalCurrentExercise(): Int? {
        val value = sharedPref.getInt("currentExercise", -1)
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExercise(indexExercise: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExercise", indexExercise ?: -1)
        editor.apply()
    }

    override suspend fun getLocalCurrentExerciseSets(): Int? {
        val value = sharedPref.getInt("currentExerciseSets", -1)
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExerciseSets", exerciseSets ?: -1)
        editor.apply()
    }

    override suspend fun getLocalWorkouts(): List<Training> {
        return localWorkouts
    }

    override suspend fun setLocalWorkouts(workouts: List<Training>) {
        localWorkouts = workouts
    }

    override suspend fun getLocalCurrentWorkout(): Training? {
        return localCurrentWorkout.value
    }

    override suspend fun setLocalCurrentWorkout(workout: Training?) {
        localCurrentWorkout.value = workout
    }

    override suspend fun getLocalPastWorkouts(): List<Training> {
        return localPastWorkouts
    }

    override suspend fun setLocalPastWorkouts(workouts: List<Training>) {
        localPastWorkouts = workouts
    }

}