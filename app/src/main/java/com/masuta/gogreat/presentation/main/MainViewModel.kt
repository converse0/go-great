package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
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
            repository.startTraining("a7c49f05-d711-4dfb-a605-ec56e1681324")
            val resp = repository.findAll()
            val trainMap: MutableMap<String, String?> = mutableMapOf()
            resp.data?.let { it ->
                it.forEach {
                    trainMap[it.uid!!] = null
                }
                trainMap.keys.forEach { uid->
                    val resp2 = repository.getTrainingDetail(uid)
                    println("resp2: $resp2")
                    resp2.image?.let {
                        trainMap[uid] = it
                    }
                }
                println("trainMap: $trainMap")
                it.forEach {
                    it.image = trainMap[it.uid!!]
                }
                list.value = it

            }
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