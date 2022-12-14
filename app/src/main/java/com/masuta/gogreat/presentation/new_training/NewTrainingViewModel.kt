package com.masuta.gogreat.presentation.new_training

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.core.handlers.train_handlers.TrainHandlers
import com.masuta.gogreat.core.model.Training
import com.masuta.gogreat.core.model.TrainingExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTrainingViewModel @Inject constructor(
    private val trainHandlers: TrainHandlers,
): ViewModel() {

   suspend fun saveTrain(newTrain: Training) {
       trainHandlers.saveWorkout(newTrain)
    }

    fun getLocalExercises(list: MutableState<List<TrainingExercise>>) {
        viewModelScope.launch {
            val data = trainHandlers.getAllExercise()
            list.value = data
        }
    }

    fun clearLocalExercises() {
        viewModelScope.launch {
            trainHandlers.clearExerciseData()
        }
    }
}