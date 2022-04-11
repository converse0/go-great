package com.masuta.gogreat.presentation.new_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTrainingViewModel @Inject constructor(
    private val repository: TrainRepository
): ViewModel() {

    fun saveTrain(newTrain: Training) {
        viewModelScope.launch {
            repository.save(newTrain)
        }
    }

}