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
class StartTrainingViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    fun getExerciseById(id: String): TrainingExercise {
        var exercise: TrainingExercise = TrainingExercise(5, "5s", 0,
            0, "Exercise", "5s", "other", "", )

        viewModelScope.launch {
            val resp = repository.getLocalEx(id.toInt())
            println("Exercise: $resp")

            resp?.let {
                exercise = it
            }
        }
        return exercise
    }
}