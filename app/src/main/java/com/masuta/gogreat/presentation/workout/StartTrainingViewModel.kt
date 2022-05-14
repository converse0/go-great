package com.masuta.gogreat.presentation.workout

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.masuta.gogreat.R
import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.handlers.train_handlers.*
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.utils.ListsValuesForSliders
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
    private val listValuesForSliders: ListsValuesForSliders,
    private val store: TrainStore,
    private val trainHandlers: TrainHandlers,
): ViewModel() {

    val listRelax = listValuesForSliders.getRelaxList
    val listDuration = listValuesForSliders.getDurationList

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
            trainHandlers.endTraining()
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

    private fun onEvent(event: TrainingEvent) {
        if (_indexExercise.value == _listExercises.value.size) {
           return
        }
        when(event) {
            is TrainingEvent.NextSet -> {
                viewModelScope.launch {
                    _exerciseSets.value--
                    store.setLocalCurrentExerciseSets(_exerciseSets.value)
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

                    store.setLocalCurrentExercise(_indexExercise.value)
                    store.setLocalCurrentExerciseSets(_exerciseSets.value)
                }

            }
            is TrainingEvent.SetExerciseSets -> {
                _exerciseSets.value = event.sets
            }
        }
    }

    fun getTraining(uid: String) {
        viewModelScope.launch {
            val resp = trainHandlers.getTraining(uid)

            resp.localTraining?.let { it ->
                _listExercises.value = it.exercises

                resp.currentExercise?.let { exercise ->
                    _indexExercise.value = exercise
                }

                _currentExercise.value = it.exercises[indexExercise.value]
                _exerciseSets.value = resp.currentExerciseSets ?: _currentExercise.value.numberOfSets

                store.setLocalCurrentExerciseSets(_exerciseSets.value)
                store.setLocalCurrentExercise(_indexExercise.value)
            }
        }
    }

    fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        viewModelScope.launch {
            _currentExercise.value = listExercises.get(_indexExercise.value)
            _exerciseSets.value = _currentExercise.value.numberOfSets

            trainHandlers.setExerciseParameters(
                uid = uid,
                listExercises = listExercises,
                indexExercise = _indexExercise.value,
                exerciseSets = _currentExercise.value.numberOfSets
            )
        }
    }

    fun finishTraining(uid: String) {
        viewModelScope.launch {
            trainHandlers.finishTraining(uid)
        }
    }
    fun playFinalSound(context: Context) {
        val mp = MediaPlayer.create(context, R.raw.fanfar)
        mp.start()
    }
}