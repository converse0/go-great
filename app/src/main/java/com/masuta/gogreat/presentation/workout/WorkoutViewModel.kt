package com.masuta.gogreat.presentation.workout

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.handlers.train_handlers.TrainHandlers
import com.masuta.gogreat.core.model.TrainingExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val trainHandlers: TrainHandlers,
): ViewModel() {

    fun getExercises(
        uid: String,
        listExercises: MutableState<List<TrainingExercise>>,
        name: MutableState<String>
    ) {
        viewModelScope.launch {
            val resp = trainHandlers.getLocalTrainingByUid(uid)

            resp?.let {
                listExercises.value = it.exercises
                name.value = it.name
            }
        }
    }

    fun startTraining(uid: String, navController: NavHostController) {
        viewModelScope.launch {
            val resp = trainHandlers.startTraining(uid)
            resp.code?.let { code ->
                trainHandlers.errorHandler(code, resp.message, navController)
            }
        }
    }
}