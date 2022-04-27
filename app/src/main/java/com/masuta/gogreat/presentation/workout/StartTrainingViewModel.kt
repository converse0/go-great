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

    fun endTraining(navController: NavController, context: Context) {
        viewModelScope.launch{
            repository.setLocalCurrentExercise(null)
            repository.setLocalCurrentExerciseSets(null)

            repository.setLocalCurrentWorkout(null)

            repository.currentWorkoutDataReload = true
            repository.pastWorkoutsDataReload = true
            repository.workoutsDataReload = true

            playFinalSound(context)
            delay(500)
            navController.navigate("main")
        }
    }

    fun nextSetOrTraining(onOpenModal: () -> Unit) {
        viewModelScope.launch {
            onOpenModal()
            delay(200L)
            onEvent(TrainingEvent.NextSet)
            if (_exerciseSets.value == 0) {
                onEvent(TrainingEvent.NextExercise)
            }
        }
    }

    fun onEvent(event: TrainingEvent) {
        if (_indexExercise.value==_listExercises.value.size) {
          //  navigateMain()
           return
        }
        when(event) {
            is TrainingEvent.NextSet -> {
                viewModelScope.launch {
                    _exerciseSets.value--
                    println("Exercise sets: ${_exerciseSets.value}")
                    repository.setLocalCurrentExerciseSets(_exerciseSets.value)
                }
            }
            is TrainingEvent.NextExercise -> {
                    _indexExercise.value++

                if (_indexExercise.value==_listExercises.value.size) {
                    return
                }
                viewModelScope.launch {
                    _currentExercise.value = _listExercises.value[_indexExercise.value]
                    _exerciseSets.value = _listExercises.value[_indexExercise.value].numberOfSets

                    repository.setLocalCurrentExercise(_indexExercise.value)
                    repository.setLocalCurrentExerciseSets(_exerciseSets.value)
                }

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
            val exerciseCurrent = repository.getLocalCurrentExercise()
            val sets = repository.getLocalCurrentExerciseSets()

            resp?.let { it ->
                _listExercises.value = it.exercises

                exerciseCurrent?.let { exercise ->
                    println("Index EXERCISE FROM LOCAL: $exercise")
                    _indexExercise.value = exercise
                }

                _currentExercise.value = it.exercises[indexExercise.value]

                println("NUMBER OF SETS LOCAL: $sets")
                _exerciseSets.value = sets ?: _currentExercise.value.numberOfSets

                repository.setLocalCurrentExerciseSets(_exerciseSets.value)
                repository.setLocalCurrentExercise(_indexExercise.value)
            }
//
//            listExercises.value = resp.exercises
//            interval.value = resp.interval.toInteger()
        }
    }

    fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        viewModelScope.launch {
            repository.setExerciseParams(uid, listExercises)
            repository.workoutsDataReload = true
            repository.pastWorkoutsDataReload = true
            repository.currentWorkoutDataReload = true

            _currentExercise.value = listExercises.get(_indexExercise.value)
            _exerciseSets.value = _currentExercise.value.numberOfSets

            // Update local states
            repository.setLocalCurrentExercise(_indexExercise.value)
            repository.setLocalCurrentExerciseSets(_currentExercise.value.numberOfSets)

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