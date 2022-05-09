package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.data.store.TrainStore
import com.masuta.gogreat.domain.model.Training
import com.masuta.gogreat.domain.repository.TrainRepository

data class TrainResponse(
    val data: List<Training>? = null,
    val code: Int? = null,
    val message: String? = null
)

class GetWorkouts(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    private var workoutsReloadData = false
        get() = repository.workoutsDataReload
        set(value) {
            field = value
            repository.workoutsDataReload = value
        }

    suspend operator fun invoke(): TrainResponse {
        if (workoutsReloadData) {
            workoutsReloadData = false

            val resp = repository.findAll()
            resp.data?.let { training ->
                store.clearLocalTrainingData()
                training.forEach { store.saveLocalTraining(it.validateExerciseData()) }
            }
            val myTrains = repository.getMyTrainings()
            myTrains.data?.let { trains ->
                val localList = trains.map { it.validateExerciseData() }
                store.setLocalWorkouts(localList)
                return TrainResponse(data = trains)

            } ?: myTrains.code?.let { code ->
                return TrainResponse(code = code, message = resp.message)
            }
        } else {
            val resp = store.getLocalWorkouts()
            return TrainResponse(data = resp)
        }

        return TrainResponse()
    }
}