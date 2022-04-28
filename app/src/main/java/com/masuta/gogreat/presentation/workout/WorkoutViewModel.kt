package com.masuta.gogreat.presentation.workout

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    fun getExercises(
        uid: String,
        listExercises: MutableState<List<TrainingExercise>>,
        name: MutableState<String>
    ) {
        viewModelScope.launch {
            val resp = repository.getLocalTrainingByUid(uid)

            resp?.let {
                listExercises.value = it.exercises
                name.value = it.name
            }
        }
    }

    fun startTraining(uid: String) {
        viewModelScope.launch {
            repository.setLocalCurrentExercise(null)
            repository.setLocalCurrentExerciseSets(null)

            repository.startTraining(uid)
            repository.workoutsDataReload = true
            repository.pastWorkoutsDataReload = true
            repository.currentWorkoutDataReload = true
        }
    }
}