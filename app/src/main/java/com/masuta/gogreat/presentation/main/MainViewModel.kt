package com.masuta.gogreat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.handlers.train_handlers.TrainHandlers
import com.masuta.gogreat.core.model.Training
import com.masuta.gogreat.presentation.profile.routeTo
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val trainHandlers: TrainHandlers,
): ViewModel() {

    private var isDataLoad = true

    val listTrainings = mutableStateOf(emptyList<Training>())
    val currentWorkout = mutableStateOf(Training(
        name = "",
        exercises = mutableListOf(),
        interval = ""
    ))
    val listPastTrainings = mutableStateOf(emptyList<Training>())

    fun getData(navController: NavHostController) {
        viewModelScope.launch {
            if (isDataLoad) {
                isDataLoad = false
                val first = getWorkouts(navController)
                if (first) {
                    getCurrentTraining(navController)
                    getPastTrainings(navController)
                }
            }
        }
    }

   private suspend fun getWorkouts(
        navController: NavHostController
    ): Boolean {
        val resp = trainHandlers.getWorkouts()

        println("GET WORKOUTS RESP: $resp")

        resp.data?.let {
            if (listTrainings.value.isEmpty()) {
                listTrainings.value = it
            }
            return true
        } ?: resp.code?.let { code ->
            trainHandlers.errorHandler(code, resp.message, navController)
            return false
        }

        return true
    }

    private fun getCurrentTraining(
         navController: NavHostController,
     ) {
         viewModelScope.launch {
             val resp = trainHandlers.getCurrentWorkout()

             resp.data?.let {
                 it.getOrNull(0)?.let { train ->
                     if (currentWorkout.value.name.isEmpty()) {
                         currentWorkout.value = train
                     }
                 }
             } ?: resp.code?.let { code ->
                 trainHandlers.errorHandler(code, resp.message, navController)
             }
         }
    }

    private fun getPastTrainings(
        navController: NavHostController,
    ) {
        viewModelScope.launch {
            val resp = trainHandlers.getPastWorkouts()

            resp.data?.let {
                if (listPastTrainings.value.isEmpty()) {
                    listPastTrainings.value = it
                }
            } ?: resp.code?.let{ code ->
                trainHandlers.errorHandler(code, resp.message, navController)
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