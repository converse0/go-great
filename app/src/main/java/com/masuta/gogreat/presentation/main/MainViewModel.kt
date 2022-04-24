package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
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
    private var currSec:Int = 0
    private var globSec = 0
    private var count = 0
    private var job: Job? = null

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

    fun getWorkouts(
        list: MutableState<List<Training>>,
//        countTotalWorkout: MutableState<Int>
    ) {
        if (workoutsReloadData) {
            workoutsReloadData = false
            viewModelScope.launch {
                val localTrainings = repository.getAllLocalTrainings()

//            if (localTrainings != null
//                &&localTrainings.size!=list.value.size
//                &&list.value.isNotEmpty()) {
//                println("list.value: ${list.value.size}")
//                println("localTrainings: ${localTrainings.size}")
//
//                println("findAll..${list.value.size}..")
//
//                val resp = repository.findAll()
//                println("fa size..${resp.data?.size ?: 0}..")
//                repository.clearLocalTrainingData()
//
//                resp.data?.let { training ->
//                 //   list.value = training.map { it.validateExerciseData() }
//                    training.forEach { repository.saveLocal(it.validateExerciseData()) }
//                }
//
//                val myTrains = repository.getMyTrainings()
//                myTrains?.let { train ->
//                    list.value = train.map { it.validateExerciseData() }
//                }
//            }
//            else if (localTrainings != null
//                &&list.value.isEmpty()) {
//                list.value = localTrainings
//                println("localTraining (mem 0): ${localTrainings.size}")
//            }
                if(1==1) {
                    println("findAll....")
                    val resp = repository.findAll()
                    resp.data?.let { training ->
//                    list.value = training.map { it.validateExerciseData() }
                        repository.clearLocalTrainingData()
                        training.forEach { repository.saveLocal(it.validateExerciseData()) }
                    }
                    val myTrains = repository.getMyTrainings()
                    myTrains?.let { trains ->
                        println("myTrains: ${trains.size}")
                        list.value = trains.map { it.validateExerciseData() }
                    }
                }
                //    countTotalWorkout.value ++
            }
        }
    }
     fun getCurrentTraining(
         training: MutableState<Training>,
//         countCurrentWorkout: MutableState<Int>
     ) {
         if(currentWorkoutReloadData) {
             currentWorkoutReloadData = false
            viewModelScope.launch {
                repository.getCurrentTraining()?.let {
                    training.value = it.validateExerciseData()
                }
            //   countCurrentWorkout.value++
            }
         }
    }

    fun getPastTrainings(list: MutableState<List<Training>>) {
        if (pastWorkoutsReloadData) {
            pastWorkoutsReloadData = false
            println("past Workouts Data Reload: ${repository.pastWorkoutsDataReload}")
            viewModelScope.launch {
                val measureTime = measureTimeMillis {
                    val resp = repository.getPassTrainings()
                    resp?.let {
                        list.value = it
                    }
                }
                println("getPastTrainings: ${measureTime}")
            }
        }
    }

    fun clearLocalExercises() {
        viewModelScope.launch {
            repository.clearLocalExerciseData()
        }
    }

    fun init(sec:Int) {
        globSec = sec
    }

    fun playSound(context: Context) {
        val mp =  MediaPlayer.create(context, R.raw.beep).start()
    }

    fun start( text: MutableState<String>, context: Context) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val seq = 0..globSec - count
            for (i in seq.reversed()) {
                currSec = i
                count++
                if(i % 10 == 0) playSound(context)
                text.value = i.toString()
                delay(1000)
            }

        }
    }
    fun stop() {
        job?.cancel()
    }
}