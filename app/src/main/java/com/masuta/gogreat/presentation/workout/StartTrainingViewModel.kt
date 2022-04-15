package com.masuta.gogreat.presentation.workout

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import com.masuta.gogreat.presentation.new_training.toInteger
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

    fun onEvent(event: TrainingEvent) {
        when(event) {
            is TrainingEvent.NextSet -> {
                _exerciseSets.value--
            }
            is TrainingEvent.NextExercise -> {
                if (_indexExercise.value+1 <= _listExercises.value.size-1) {
                    println("old index: ${_indexExercise.value}")
                    println(_listExercises.value[_indexExercise.value])
                    println(_listExercises.value[_indexExercise.value].numberOfSets)
                    _indexExercise.value++
                    println("new index: ${_indexExercise.value}")

                    _currentExercise.value = _listExercises.value[_indexExercise.value]
                    _exerciseSets.value = _listExercises.value[_indexExercise.value].numberOfSets
                } else {
                    println("end of training")
                }
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
        }
    }
}