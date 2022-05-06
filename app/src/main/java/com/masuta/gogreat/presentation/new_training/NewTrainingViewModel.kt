package com.masuta.gogreat.presentation.new_training

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTrainingViewModel @Inject constructor(
    private val repository: TrainRepository,
    private val store: TrainStore
): ViewModel() {

   suspend fun saveTrain(newTrain: Training) {
            repository.save(newTrain)
            repository.workoutsDataReload = true
            repository.pastWorkoutsDataReload = true
            repository.currentWorkoutDataReload = true
    }

    fun getLocalExercises(list: MutableState<List<TrainingExercise>>) {
        viewModelScope.launch {
            val data = store.getAllLocalEx()
            list.value = data
        }
    }

    fun clearLocalExercises() {
        viewModelScope.launch {
            store.clearLocalExerciseData()
        }
    }
}