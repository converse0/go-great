package com.masuta.gogreat.presentation.new_training

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    fun getExercises(id: Long, exercisesList: MutableState<List<TrainingExercise>>) {
        viewModelScope.launch {
            val resp = repository.findById(id)
            println(resp)

            if(resp.data != null) {
                exercisesList.value = resp.data
            }
        }
    }
}
