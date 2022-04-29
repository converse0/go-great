package com.masuta.gogreat.presentation.main

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.masuta.gogreat.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(): ViewModel() {

    private var currSec:Int = 0
    private var globSec = 0
    private var count = 0
    private var job: Job? = null

    fun init(sec:Int) {
        globSec = sec
        currSec = 0
        count = 0
        job = null
    }

    fun start(text: MutableState<String>, context: Context, onTimerEnd: () -> Unit) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val seq = 0..globSec - count
            for (i in seq.reversed()) {
                currSec = i
                count++
                if(i <= 5) playSound(context)
                if(i == 0) {
                    stop()
                    currSec = 0
                    globSec = 0
                    count = 0
                    job = null
                    onTimerEnd()
                    delay(500)
                }
                text.value = i.toString()
                delay(1000)
            }
        }
    }
    fun stop() {
        job?.cancel()
    }

    fun playSound(context: Context) {
        val mp =  MediaPlayer.create(context, R.raw.zvuk41).start()
    }

    fun playFinalSound(context: Context) {
        val mp = MediaPlayer.create(context, R.raw.fanfar)
        mp.start()
    }
}