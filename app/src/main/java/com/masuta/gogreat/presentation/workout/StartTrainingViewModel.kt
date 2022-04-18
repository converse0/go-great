package com.masuta.gogreat.presentation.workout

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var _exerciseSets = mutableStateOf(0)
    var exerciseSets: State<Int> = _exerciseSets

    private var _currentExercise = mutableStateOf(default)
    var currentExercise: State<TrainingExercise> = _currentExercise

    var interval: String? = null

    fun onEvent(event: TrainingEvent, navigateMain: () -> Unit) :Boolean {
        if (_indexExercise.value==_listExercises.value.size) {
            _indexExercise.value++
          //  navigateMain()
            println("=====================FINISHED=====================")
            return false
        }
        when(event) {
            is TrainingEvent.NextSet -> {
                _exerciseSets.value--
            }
            is TrainingEvent.NextExercise -> {

                    println("old index: ${_indexExercise.value}")
                    println("size: ${_listExercises.value.size}")
                    println(_listExercises.value[_indexExercise.value])
                    println(_listExercises.value[_indexExercise.value].numberOfSets)
                    _indexExercise.value++
                if (_indexExercise.value>=_listExercises.value.size) {

                    println("=====================FINISHED=====================")
                    return false
                }
                    println("new index: ${_indexExercise.value}")

                    _currentExercise.value = _listExercises.value[_indexExercise.value]
                    _exerciseSets.value = _listExercises.value[_indexExercise.value].numberOfSets

                println("index: ${_indexExercise.value}")
            }
            is TrainingEvent.SetExerciseSets -> {
                _exerciseSets.value = event.sets
            }
        }
        return true
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

            repository
        }
    }

    fun playSound(context: Context) {
        val mp =  MediaPlayer.create(context, R.raw.zvuk41).start()
    }
}