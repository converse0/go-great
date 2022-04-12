package com.masuta.gogreat.presentation.workout

import androidx.lifecycle.ViewModel
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

}