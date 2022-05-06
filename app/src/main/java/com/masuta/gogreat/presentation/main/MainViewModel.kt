package com.masuta.gogreat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.data.store.TrainStore
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
    private val store: TrainStore
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
                    store.clearLocalTrainingData()
                    training.forEach { store.saveLocalTraining(it.validateExerciseData()) }
                }
                val myTrains = repository.getMyTrainings()
                myTrains.data?.let { trains ->
                    val localList = trains.map { it.validateExerciseData() }
                    list.value = localList
                    store.setLocalWorkouts(localList)
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
                val resp = store.getLocalWorkouts()
                println("MY WORKOUTS LOCAL : $resp")
                list.value = resp
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
                         store.setLocalCurrentWorkout(train.validateExerciseData())
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
                 store.getLocalCurrentWorkout()?.let {
                     println("CURRENT WORKOUT LOCAL : $it")
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
                        store.setLocalPastWorkouts(it)
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
                val resp = store.getLocalPastWorkouts()
                println("PAST WORKOUTS LOCAL :$resp")
                list.value = resp
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
            store.setLocalWorkouts(localList)
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
            store.setLocalPastWorkouts(pastTrainings)
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
                store.setLocalCurrentWorkout(train.validateExerciseData())
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
            store.clearLocalExerciseData()
        }
    }
}