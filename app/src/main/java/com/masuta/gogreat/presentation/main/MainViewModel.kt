package com.masuta.gogreat.presentation.main

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    var workoutsReloadData = false
        get() = repository.workoutsDataReload
        set(value) {
            field = value
            repository.workoutsDataReload = value
        }

    var currentWorkoutReloadData = false
        get() = repository.currentWorkoutDataReload
        set(value) {
            field = value
            repository.currentWorkoutDataReload = value
        }

    var pastWorkoutsReloadData = false
        get() = repository.pastWorkoutsDataReload
        set(value) {
            field = value
            repository.pastWorkoutsDataReload = value
        }

    fun getWorkouts(list: MutableState<List<Training>>) {
        if (workoutsReloadData) {
            workoutsReloadData = false

            viewModelScope.launch {
                val resp = repository.findAll()
                resp.data?.let { training ->
                    repository.clearLocalTrainingData()
                    training.forEach { repository.saveLocal(it.validateExerciseData()) }
                }
                val myTrains = repository.getMyTrainings()
                myTrains?.let { trains ->

                    val localList = trains.map { it.validateExerciseData() }
                    println("localList: ${localList.size}")
                    list.value = localList
                    repository.setLocalWorkouts(localList)
                }
            }
        } else {
            viewModelScope.launch {
                list.value = repository.getLocalWorkouts()
            }
        }
    }
     fun getCurrentTraining(training: MutableState<Training>) {
         if(currentWorkoutReloadData) {
             currentWorkoutReloadData = false
             viewModelScope.launch {
                repository.getCurrentTraining()?.let {

                    val workout = it.validateExerciseData()
                    training.value = workout
                    repository.setLocalCurrentWorkout(workout)
                }
            }
         } else {
             viewModelScope.launch {
                 repository.getLocalCurrentWorkout()?.let {
                    training.value = it
                 }
             }
         }
    }

    fun getPastTrainings(list: MutableState<List<Training>>) {
        if (pastWorkoutsReloadData) {
            pastWorkoutsReloadData = false
            viewModelScope.launch {
                measureTimeMillis {
                    val resp = repository.getPassTrainings()
                    resp?.let {
                        list.value = it
                        repository.setLocalPastWorkouts(it)
                    }
                }
            }
        } else {
            viewModelScope.launch {
                list.value = repository.getLocalPastWorkouts()
            }
        }
    }

    fun clearLocalExercises() {
        viewModelScope.launch {
            repository.clearLocalExerciseData()
        }
    }
}