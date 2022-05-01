package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.masuta.gogreat.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(): ViewModel() {

    var currSec by mutableStateOf(0)
    private var globSec = 0
    private var count = 0
    var job by mutableStateOf<Job?>(null)

    fun init(sec:Int) {
        globSec = sec
    }

    fun start(text: MutableState<String>, context: Context, onTimerEnd: () -> Unit) {

        job = CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            val seq = 0..globSec - count
            for (i in seq.reversed()) {
                currSec = i
                count++
                if(i in 1..5) launch {playSound(context)}
                if(i == 0) {
                    text.value = "0"
                    delay(500)
                    stop(onTimerEnd)
                }
                text.value = i.toString()
                delay(1000)
            }
        }
    }
    fun pause() {
        job?.cancel()
    }

    fun stop(onTimerEnd: () -> Unit) {
        onTimerEnd()
        job?.cancel()
        currSec = 0
        count = 0
        globSec = 0
        job = null
    }

    private fun playSound(context: Context) {
        MediaPlayer.create(context, R.raw.zvuk41).start()
    }

    fun playFinalSound(context: Context) {
        val mp = MediaPlayer.create(context, R.raw.fanfar)
        mp.start()
    }
}