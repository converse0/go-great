package com.masuta.gogreat.core.store

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import javax.inject.Inject

class TrainStoreImpl @Inject constructor(
    private val context: Context
) : TrainStore {

    private var localTraining:Map<String,Training> = mutableMapOf()
    private var localTrainingEx: Map<Int, TrainingExercise> = mutableMapOf()

    private var localWorkouts: List<Training>? = null //mutableListOf()
    private var localCurrentWorkout = mutableStateOf<Training?>(null)
    private var localPastWorkouts: List<Training>? = null //mutableListOf()

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

    override suspend fun getLocalWorkouts(): List<Training>? {
        return localWorkouts
    }

    override suspend fun setLocalWorkouts(workouts: List<Training>?) {
        localWorkouts = workouts
    }

    override suspend fun getLocalCurrentWorkout(): Training? {
        return localCurrentWorkout.value
    }

    override suspend fun setLocalCurrentWorkout(workout: Training?) {
        localCurrentWorkout.value = workout
    }

    override suspend fun getLocalPastWorkouts(): List<Training>? {
        return localPastWorkouts
    }

    override suspend fun setLocalPastWorkouts(workouts: List<Training>?) {
        localPastWorkouts = workouts
    }

    override suspend fun saveLocalTraining(newTrain: Training): String {
        val id = newTrain.uid ?: ""
        localTraining.get(id)?.apply {
            this.image = newTrain.image
            this.uid = id
            this.name= newTrain.name
            this.exercises = newTrain.exercises

        } ?: run {
            localTraining = localTraining.plus(id to newTrain)
        }
        return id
    }

    override suspend fun getAllLocalTrainings(): List<Training>? {
        return if (localTraining.isNotEmpty()) {
            localTraining.values.toList()
        } else {
            null
        }
    }

    override suspend fun getLocalTrainingByUid(uid: String): Training? {
        localTraining.get(uid).let {
            return it
        }
    }

    override suspend fun clearLocalTrainingData() {
        localTraining = mutableMapOf()
    }

    override suspend fun saveLocalEx(ex: TrainingExercise): Int {
        val id = localTrainingEx.size.plus(1)
        localTrainingEx = localTrainingEx.plus(id to ex)
        return id
    }

    override suspend fun getLocalEx(id: Int): TrainingExercise? {
        localTrainingEx.get(id).let {
            return it
        }
    }

    override suspend fun getAllLocalEx(): List<TrainingExercise> {
        return localTrainingEx.values.toList()
    }

    override suspend fun clearLocalExerciseData() {
        localTrainingEx = mutableMapOf()
    }

}