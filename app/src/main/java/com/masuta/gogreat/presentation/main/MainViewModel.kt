package com.masuta.gogreat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository
import com.masuta.gogreat.presentation.profile.routeTo
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TrainRepository,
): ViewModel() {

    private var workoutsReloadData = false
        get() = repository.workoutsDataReload
        set(value) {
            field = value
            repository.workoutsDataReload = value
        }

    private var currentWorkoutReloadData = false
        get() = repository.currentWorkoutDataReload
        set(value) {
            field = value
            repository.currentWorkoutDataReload = value
        }

    private var pastWorkoutsReloadData = false
        get() = repository.pastWorkoutsDataReload
        set(value) {
            field = value
            repository.pastWorkoutsDataReload = value
        }

    fun getWorkouts(
        list: MutableState<List<Training>>,
        context: Context,
        navController: NavHostController
    ) {
        if (workoutsReloadData) {
            workoutsReloadData = false

            viewModelScope.launch {
                val resp = repository.findAll()
                resp.data?.let { training ->
                    repository.clearLocalTrainingData()
                    training.forEach { repository.saveLocal(it.validateExerciseData()) }
                }
                val myTrains = repository.getMyTrainings()
                myTrains.data?.let { trains ->
                    val localList = trains.map { it.validateExerciseData() }
                    list.value = localList
                    repository.setLocalWorkouts(localList)
                } ?: myTrains.code?.let { code ->
                    when(val error = handleErrors(code)) {
                        is Timeout -> {
                            Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                routeTo(navController, error.errRoute)
                                Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                list.value = repository.getLocalWorkouts()
            }
        }
    }

     fun getCurrentTraining(
         training: MutableState<Training>,
         context: Context,
         navController: NavHostController
     ) {
         if(currentWorkoutReloadData) {
             currentWorkoutReloadData = false
             viewModelScope.launch {
                val resp = repository.getCurrentTraining()
                 resp.data?.let {
                     it.getOrNull(0)?.let { train ->
                         training.value = train
                         repository.setLocalCurrentWorkout(train.validateExerciseData())
                     }
                 } ?: resp.code?.let { code ->
                     when(val error = handleErrors(code)) {
                         is Timeout -> {
                             Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                         }
                         else -> {
                             withContext(Dispatchers.Main) {
                                 routeTo(navController, error.errRoute)
                                 Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                             }
                         }
                     }
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

    fun getPastTrainings(
        list: MutableState<List<Training>>,
        context: Context,
        navController: NavHostController
    ) {
        if (pastWorkoutsReloadData) {
            pastWorkoutsReloadData = false
            viewModelScope.launch {
                measureTimeMillis {
                    val resp = repository.getPassTrainings()
                    resp.data?.let{
                        list.value = it
                        repository.setLocalPastWorkouts(it)
                    } ?: resp.code?.let { code ->
                        when(val error = handleErrors(code)) {
                            is Timeout -> {
                                Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                withContext(Dispatchers.Main) {
                                    routeTo(navController, error.errRoute)
                                    Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                list.value = repository.getLocalPastWorkouts()
            }
        }
    }

    suspend fun getMyTrainings(
        context: Context,
        navController: NavHostController
    ){
       val resp = repository.getMyTrainings()
        resp.data?.let { trains->
            val localList = trains.map { it.validateExerciseData() }
            repository.setLocalWorkouts(localList)
        } ?: resp.code?.let { code ->
            when(val error = handleErrors(code)) {
                is Timeout -> {
                    Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        routeTo(navController, error.errRoute)
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    suspend fun getPastTrainings(
        context: Context,
        navController: NavHostController
    ) {
        val resp = repository.getPassTrainings()
        resp.data?.let { pastTr ->
            val pastTrainings = pastTr.map { it.validateExerciseData() }
            repository.setLocalPastWorkouts(pastTrainings)
        } ?: resp.code?.let { code ->
            when(val error = handleErrors(code)) {
                is Timeout -> {
                    Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        routeTo(navController, error.errRoute)
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
//                    routeTo(navController, error.errRoute)
                }
            }
        }
    }

    suspend fun getCurrentTraining(
        context: Context,
        navController: NavHostController
    ) {
        val resp = repository.getCurrentTraining()
        resp.data?.let {
            it.getOrNull(0)?.let { train ->
                repository.setLocalCurrentWorkout(train.validateExerciseData())
            }
        } ?: resp.code?.let { code ->
                when(val error = handleErrors(code)) {
                    is Timeout -> {
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            routeTo(navController, error.errRoute)
                            Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }


    fun clearLocalExercises() {
        viewModelScope.launch {
            repository.clearLocalExerciseData()
        }
    }
}