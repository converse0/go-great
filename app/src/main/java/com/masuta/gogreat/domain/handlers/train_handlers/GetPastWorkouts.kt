package com.masuta.gogreat.domain.handlers.train_handlers

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.repository.TrainRepository
import kotlin.system.measureTimeMillis

class GetPastWorkouts(
    private val repository: TrainRepository,
    private val store: TrainStore
) {

    private var pastWorkoutsReloadData = false
        get() = repository.pastWorkoutsDataReload
        set(value) {
            field = value
            repository.pastWorkoutsDataReload = value
        }

    suspend operator fun invoke(): TrainResponse {
        if (pastWorkoutsReloadData) {
            pastWorkoutsReloadData = false
            measureTimeMillis {
                val resp = repository.getPassTrainings()
                resp.data?.let{
                    store.setLocalPastWorkouts(it)
                    return TrainResponse(data = it)
                } ?: resp.code?.let { code ->
                    return TrainResponse(code = code, message = resp.message)
                }
            }
        } else {
            val resp = store.getLocalPastWorkouts()
            return TrainResponse(data = resp)
        }

        return TrainResponse()
    }

}