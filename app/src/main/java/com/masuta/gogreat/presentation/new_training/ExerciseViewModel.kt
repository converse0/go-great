package com.masuta.gogreat.presentation.new_training

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import com.masuta.gogreat.presentation.profile.routeTo
import com.masuta.gogreat.utils.Errors
import com.masuta.gogreat.utils.ListsValuesForSliders
import com.masuta.gogreat.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: TrainRepository,
    private val listValuesForSliders: ListsValuesForSliders,
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
            val resp = repository.findById(id)

            resp.data?.let {
                exercisesList.value = resp.data
            } ?: resp.code?.let { code ->

//                val error = handleErrors(code)
//
//                when(error) {
//                    is Errors.Timeout -> {
//                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
//                    }
//                    else -> {
//                        isRoute.value = true
//                        routeTo(navController, error.)
//                    }
//                }

                when(code) {
                    16 -> {
                        isRoute.value = true
                        routeTo(navController, "sign-in")
                    }
                    2, 5, 13 -> {
                        isRoute.value = true
                        routeTo(navController, "about")
                    }
                    777 -> {
                        Toast.makeText(context, resp.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun saveLocalExercise(exercise: TrainingExercise) {
        viewModelScope.launch {
            repository.saveLocalEx(exercise)
        }
    }
}
