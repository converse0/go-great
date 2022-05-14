package com.masuta.gogreat.core.service.train_service

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository

class TrainServiceImpl(
    private val repository: TrainRepository,
    private val store: TrainStore
): TrainService {

    private var currentWorkoutReloadData = false
        get() = repository.currentWorkoutDataReload
        set(value) {
            field = value
            repository.currentWorkoutDataReload = value
        }

    private var workoutsReloadData = false
        get() = repository.workoutsDataReload
        set(value) {
            field = value
            repository.workoutsDataReload = value
        }

    private var pastWorkoutsReloadData = false
        get() = repository.pastWorkoutsDataReload
        set(value) {
            field = value
            repository.pastWorkoutsDataReload = value
        }

    override suspend fun getCurrentWorkout(): TrainResponse {
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

    override suspend fun getWorkouts(): TrainResponse {
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

    override suspend fun getPastWorkouts(): TrainResponse {
        if (pastWorkoutsReloadData) {
            pastWorkoutsReloadData = false
            val resp = repository.getPassTrainings()
            resp.data?.let{
                store.setLocalPastWorkouts(it)
                return TrainResponse(data = it)
            } ?: resp.code?.let { code ->
                return TrainResponse(code = code, message = resp.message)
            }
        } else {
            val resp = store.getLocalPastWorkouts()
            return TrainResponse(data = resp)
        }

        return TrainResponse()
    }

    override suspend fun saveTraining(newTrain: Training) {
        repository.save(newTrain)
        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true
    }

    override suspend fun startTraining(uid: String) {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        repository.startTraining(uid)

        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true
    }

    override suspend fun finishTraining(uid: String) {
        repository.finishTraining(uid)
    }

    override suspend fun endTraining() {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        store.setLocalCurrentWorkout(null)

        repository.currentWorkoutDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.workoutsDataReload = true
    }

    override suspend fun getLocalTrainings(uid: String): GetTrainingResponse {
        val training = store.getLocalTrainingByUid(uid)
        val exerciseCurrent = store.getLocalCurrentExercise()
        val sets = store.getLocalCurrentExerciseSets()

        return GetTrainingResponse(training, exerciseCurrent, sets)
    }

    override suspend fun getExercisesById(id: Long): ExerciseResponse {
        return repository.findById(id)
    }

    override suspend fun setExerciseParameters(params: SetExerciseParamsRequest) {
        repository.setExerciseParams(params.uid, params.listExercises)
        repository.workoutsDataReload = true
        repository.pastWorkoutsDataReload = true
        repository.currentWorkoutDataReload = true

        store.setLocalCurrentExercise(params.indexExercise)
        store.setLocalCurrentExerciseSets(params.exerciseSets)

        val training = store.getLocalTrainingByUid(params.uid).let {
            it?.copy(
                exercises = params.listExercises
            )
        }
        training?.let {
            store.saveLocalTraining(it)
        }
    }

}