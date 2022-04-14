package com.masuta.gogreat.presentation.workout

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.R
import com.masuta.gogreat.domain.model.TrainingExercise
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartTrainingViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    fun getExercises(uid: String, listExercises: MutableState<List<TrainingExercise>>) {
        viewModelScope.launch {
            val resp = repository.getTrainingDetail(uid)
            println("Exercise: $resp")

            listExercises.value = resp.exercises
        }
    }

    fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        viewModelScope.launch {
            repository.setExerciseParams(uid, listExercises)
        }
    }
}