package com.masuta.gogreat.presentation.new_training

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.handlers.train_handlers.GetExercisesById
import com.masuta.gogreat.domain.handlers.train_handlers.TrainHandlers
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.utils.ListsValuesForSliders
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val listValuesForSliders: ListsValuesForSliders,
    private val store: TrainStore,
    private val trainHandlers: TrainHandlers
): ViewModel() {

    val isRoute = mutableStateOf(false)

    val listRelax = listValuesForSliders.getRelaxList
    val listDuration = listValuesForSliders.getDurationList
    val listCounts = listValuesForSliders.getCountsList
    val listRepetitions = listValuesForSliders.getRepetitionsList
    val listSets = listValuesForSliders.getStepsList

    fun getExercises(
        id: Long,
        exercisesList: MutableState<List<TrainingExercise>>,
        navController: NavHostController,
        context: Context,
        routeTo: (navController: NavHostController, route: String) -> Unit,
    ) {
        viewModelScope.launch {
            val resp = trainHandlers.getExercisesById(id)

            resp.data?.let {
                exercisesList.value = resp.data
            } ?: resp.code?.let { code ->
                when(val error = handleErrors(code)) {
                    is Timeout -> {
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        isRoute.value = true
                        withContext(Dispatchers.Main) {
                            routeTo(navController, error.errRoute)
                            Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun saveLocalExercise(exercise: TrainingExercise) {
        viewModelScope.launch {
            store.saveLocalEx(exercise)
        }
    }
}
