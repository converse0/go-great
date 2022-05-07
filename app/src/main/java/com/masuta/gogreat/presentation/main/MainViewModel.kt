package com.masuta.gogreat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.handlers.train_handlers.GetCurrentWorkout
import com.masuta.gogreat.domain.handlers.train_handlers.GetPastWorkouts
import com.masuta.gogreat.domain.handlers.train_handlers.GetWorkouts
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.presentation.profile.routeTo
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val store: TrainStore,
    private val getMyWorkouts: GetWorkouts,
    private val getMyCurrentWorkout: GetCurrentWorkout,
    private val getMyPastWorkouts: GetPastWorkouts
): ViewModel() {

    fun getWorkouts(
        list: MutableState<List<Training>>,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val resp = getMyWorkouts()

            resp.data?.let {
                list.value = it
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
         training: MutableState<Training>,
         context: Context,
         navController: NavHostController
     ) {

         viewModelScope.launch {
            val resp = getMyCurrentWorkout()

             resp.data?.let {
                 it.getOrNull(0)?.let { train ->
                     training.value = train
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
        list: MutableState<List<Training>>,
        context: Context,
        navController: NavHostController
    ) {

        viewModelScope.launch {
            val resp = getMyPastWorkouts()

            resp.data?.let {
                list.value = it
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

    suspend fun getMyTrainings(
        context: Context,
        navController: NavHostController
    ){
        val resp = getMyWorkouts()
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

    suspend fun getPastTrainings(
        context: Context,
        navController: NavHostController
    ) {
        val resp = getMyPastWorkouts()
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

    suspend fun getCurrentTraining(
        context: Context,
        navController: NavHostController
    ) {
        val resp = getMyCurrentWorkout()
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
            store.clearLocalExerciseData()
        }
    }
}