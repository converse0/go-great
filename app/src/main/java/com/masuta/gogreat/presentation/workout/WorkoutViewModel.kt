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

    fun getExercises(uid: String, listExercises: MutableState<List<TrainingExercise>>, name: MutableState<String>) {
        viewModelScope.launch {
            val resp = repository.getTrainingDetail(uid)
            println(resp)

            listExercises.value = resp.exercises
            name.value = resp.name

//            resp.exercises.forEach { exercise ->
//                repository.saveLocalEx(exercise)
//            }

            // Test
//            println("local exercises: ${repository.getAllLocalEx()}")
        }
    }
}