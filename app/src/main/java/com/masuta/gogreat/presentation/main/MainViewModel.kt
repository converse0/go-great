package com.masuta.gogreat.presentation.main

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {
    private var currSec:Int = 0
    private var globSec = 0
    private var count = 0
    private var job: Job? = null
    fun init(sec:Int) {
        globSec = sec
    }
    fun start( text: MutableState<String>) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val seq = 0..globSec - count
            for (i in seq.reversed()) {
                currSec = i
                count++
                if(i % 10 == 0) println("ping $i")
                text.value = i.toString()
                delay(1000)
            }

        }
    }
    fun stop() {

        job?.cancel()
    }
}