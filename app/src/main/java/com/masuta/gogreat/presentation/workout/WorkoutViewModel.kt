package com.masuta.gogreat.presentation.workout

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.handlers.train_handlers.StartTraining
import com.masuta.gogreat.domain.model.TrainingExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val startTrain: StartTraining,
    private val store: TrainStore
): ViewModel() {

    fun getExercises(
        uid: String,
        listExercises: MutableState<List<TrainingExercise>>,
        name: MutableState<String>
    ) {
        viewModelScope.launch {
            val resp = store.getLocalTrainingByUid(uid)

            resp?.let {
                listExercises.value = it.exercises
                name.value = it.name
            }
        }
    }

    fun startTraining(uid: String) {
        viewModelScope.launch {
            startTrain(uid)
        }
    }
}