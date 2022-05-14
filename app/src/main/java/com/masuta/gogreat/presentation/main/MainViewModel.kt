package com.masuta.gogreat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.handlers.train_handlers.TrainHandlers
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.profile.routeTo
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val trainHandlers: TrainHandlers
): ViewModel() {

    val listTrainings = mutableStateOf(emptyList<Training>())
    val currentWorkout = mutableStateOf(Training(
        name = "",
        exercises = mutableListOf(),
        interval = ""
    ))
    val listPastTrainings = mutableStateOf(emptyList<Training>())

    fun getWorkouts(
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val resp = trainHandlers.getWorkouts()

            resp.data?.let {
                listTrainings.value = it
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

     fun getCurrentTraining(
         context: Context,
         navController: NavHostController,
     ) {
         viewModelScope.launch {
            val resp = trainHandlers.getCurrentWorkout()

             resp.data?.let {
                 it.getOrNull(0)?.let { train ->
                     currentWorkout.value = train
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
    }

    fun getPastTrainings(
        context: Context,
        navController: NavHostController,
    ) {
        viewModelScope.launch {
            val resp = trainHandlers.getPastWorkouts()

            resp.data?.let {
                listPastTrainings.value = it
            } ?: resp.code?.let{ code ->
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

    suspend fun getMyTrainingsStart(
        context: Context,
        navController: NavHostController
    ){

        val resp = trainHandlers.getWorkouts()
        resp.code?.let { code ->
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

    suspend fun getPastTrainingsStart(
        context: Context,
        navController: NavHostController
    ) {

        val resp = trainHandlers.getPastWorkouts()
        resp.code?.let { code ->
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

    suspend fun getCurrentTrainingStart(
        context: Context,
        navController: NavHostController
    ) {

        val resp = trainHandlers.getCurrentWorkout()
        resp.code?.let { code ->
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
            trainHandlers.clearLocalExerciseData()
        }
    }
}