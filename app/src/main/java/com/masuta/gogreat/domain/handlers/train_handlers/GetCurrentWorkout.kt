package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository

class GetCurrentWorkout(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    private var currentWorkoutReloadData = false
        get() = repository.currentWorkoutDataReload
        set(value) {
            field = value
            repository.currentWorkoutDataReload = value
        }

    suspend operator fun invoke(): TrainResponse {
        var currentTrain: List<Training>? = null
        if(currentWorkoutReloadData) {
            currentWorkoutReloadData = false
            val resp = repository.getCurrentTraining()
            resp.data?.let {
                it.getOrNull(0)?.let { train ->
                    store.setLocalCurrentWorkout(train.validateExerciseData())
                    currentTrain = listOf(train)
                }
                return TrainResponse(data = it)
            } ?: resp.code?.let { code ->
                return TrainResponse(code = code, message = resp.message)
            }
        } else {
            store.getLocalCurrentWorkout()?.let {
                currentTrain = listOf(it)
                return TrainResponse(data = currentTrain)
            }
        }
        return TrainResponse()
    }
}