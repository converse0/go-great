package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {
    private var currSec:Int = 0
    private var globSec = 0
    private var count = 0
    private var job: Job? = null

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

    fun getExercises(list: MutableState<List<Training>>) {
        viewModelScope.launch {
            val localTrainings = repository.getAllLocalTrainings()
            if (localTrainings != null) {
                list.value = localTrainings
            } else {
                val resp = repository.findAll()
                resp.data?.let { training ->
                    list.value = training
                    training.forEach { repository.saveLocal(it) }
                }
            }
//            val trainMap: MutableMap<String, String?> = mutableMapOf()
            println("Local Trainings: $localTrainings")
        }
    }
     fun getCurrentTraining(training: MutableState<Training>) {
        viewModelScope.launch {
            repository.getCurrentTraining()?.let {
                training.value = it
            }
        }
    }

    fun clearLocalExercises() {
        viewModelScope.launch {
            repository.clearLocalExerciseData()
        }
    }
}