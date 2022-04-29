package com.masuta.gogreat.presentation.new_training

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import com.masuta.gogreat.utils.ListsValuesForSliders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: TrainRepository,
    private val listValuesForSliders: ListsValuesForSliders
): ViewModel() {

    val listRelax = listValuesForSliders.getRelaxList()
    val listDuration = listValuesForSliders.getDurationList()

    fun getExercises(id: Long, exercisesList: MutableState<List<TrainingExercise>>) {
        viewModelScope.launch {
            val resp = repository.findById(id)

            if(resp.data != null) {
                exercisesList.value = resp.data
                println("EXERCISE ${resp.data}")
            }
        }
    }

    fun saveLocalExercise(exercise: TrainingExercise) {
        viewModelScope.launch {
            repository.saveLocalEx(exercise)
        }
    }
}
