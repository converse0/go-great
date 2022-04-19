package com.masuta.gogreat.presentation.workout

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TrainingEvent {
    object NextSet: TrainingEvent()
    object NextExercise: TrainingEvent()
    data class SetExerciseSets(val sets: Int): TrainingEvent()
}

@HiltViewModel
class StartTrainingViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    val default = TrainingExercise(0, "20s", 10, 10, "Test", "20s", "other", "")

    private val _listExercises = mutableStateOf(listOf<TrainingExercise>())
    val listExercises: State<List<TrainingExercise>> = _listExercises

    private var _indexExercise = mutableStateOf(0)
    var indexExercise: State<Int> = _indexExercise

    private var _exerciseSets = mutableStateOf(1)
    var exerciseSets: State<Int> = _exerciseSets

    private var _currentExercise = mutableStateOf(default)
    var currentExercise: State<TrainingExercise> = _currentExercise

    var interval: String? = null

    fun endTraining(navController: NavController, context: Context) {
        viewModelScope.launch{
            playFinalSound(context)
            delay(500)
            navController.navigate("main")
        }
    }

    fun nextSetOrTraining(onOpenModal: () -> Unit) {
        viewModelScope.launch {
            onOpenModal()
            delay(500L)
            onEvent(TrainingEvent.NextSet)
            if (_exerciseSets.value == 0) {
                onEvent(TrainingEvent.NextExercise)
            }
        }
    }

    fun onEvent(event: TrainingEvent) {
        if (_indexExercise.value==_listExercises.value.size) {
          //  navigateMain()
            println("=====================FINISHED=====================")
           return
        }
        when(event) {
            is TrainingEvent.NextSet -> {
                _exerciseSets.value--
            }
            is TrainingEvent.NextExercise -> {
                    println("old index: ${_indexExercise.value}")
//                    println("size: ${_listExercises.value.size}")
//                    println(_listExercises.value[_indexExercise.value])
//                    println(_listExercises.value[_indexExercise.value].numberOfSets)
                    _indexExercise.value++

                if (_indexExercise.value==_listExercises.value.size) {
                    println("=====================FINISHED=====================")
                    return
                }
                    println("new index: ${_indexExercise.value}")
                    println("list exercises: ${_listExercises.value.size}")
                    _currentExercise.value = _listExercises.value[_indexExercise.value]
                    _exerciseSets.value = _listExercises.value[_indexExercise.value].numberOfSets

                println("index: ${_indexExercise.value}")
            }
            is TrainingEvent.SetExerciseSets -> {
                _exerciseSets.value = event.sets
            }
        }
    }

    fun getTraining(uid: String) {
        viewModelScope.launch {
//            val resp = repository.getTrainingDetail(uid)
            val resp = repository.getLocalTrainingByUid(uid)
            println("Exercise: $resp")

            resp?.let { it ->
                _listExercises.value = it.exercises
//                interval.value = it.interval.toInteger()
                _currentExercise.value = it.exercises[indexExercise.value]
                _exerciseSets.value = _currentExercise.value.numberOfSets
                println("${_exerciseSets.value} ,${_indexExercise.value}")
            }
//
//            listExercises.value = resp.exercises
//            interval.value = resp.interval.toInteger()
        }
    }

    fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        viewModelScope.launch {
            repository.setExerciseParams(uid, listExercises)
            println("Old currentExercise: ${_currentExercise.value}")
            _currentExercise.value = listExercises.get(_indexExercise.value)
            _exerciseSets.value = _currentExercise.value.numberOfSets
            println("New currentExercise: ${_currentExercise.value}")

            val training = repository.getLocalTrainingByUid(uid).let {
                it?.copy(
                    exercises = listExercises
                )
            }
            training?.let {
                repository.saveLocal(it)
            }
        }
    }

    fun playSound(context: Context) {
        val mp =  MediaPlayer.create(context, R.raw.zvuk41).start()
    }

    fun finishTraining(uid: String) {
        viewModelScope.launch {
            repository.finishTraining(uid)
        }
    }
    fun playFinalSound(context: Context) {
        val mp = MediaPlayer.create(context, R.raw.fanfar)
        mp.start()

      //  mp.

    }
}