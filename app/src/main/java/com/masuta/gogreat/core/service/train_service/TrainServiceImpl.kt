package com.masuta.gogreat.core.service.train_service

import com.masuta.gogreat.core.store.TrainStore
import com.masuta.gogreat.core.handlers.train_handlers.LocalExerciseAndSets
import com.masuta.gogreat.core.model.*
import com.masuta.gogreat.core.providers.TrainRepository

class TrainServiceImpl(
    private val repository: TrainRepository,
    private val store: TrainStore
): TrainService {

    override suspend fun getCurrentWorkout(): TrainResponse {
        val localCurrentWorkout = store.getLocalCurrentWorkout()

        if (localCurrentWorkout == null) {
            val resp = repository.getCurrentTraining()
            resp.data?.let {
                val data = it.getOrNull(0)?.let { train ->
                    store.setLocalCurrentWorkout(train.validateExerciseData())
                    listOf(train)
                }
                return TrainResponse(data = data)
            } ?: resp.code?.let { code ->
                return TrainResponse(code = code, message = resp.message)
            }
        }

        return TrainResponse(data = listOf(localCurrentWorkout!!))
    }

    override suspend fun getWorkouts(): TrainResponse {

        val localWorkouts = store.getLocalWorkouts()

        if (localWorkouts == null) {
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
        }

        return TrainResponse(data = localWorkouts)
    }

    override suspend fun getPastWorkouts(): TrainResponse {

        val localPastWorkouts = store.getLocalPastWorkouts()

        if (localPastWorkouts == null) {
            val resp = repository.getPassTrainings()
            resp.data?.let{
                store.setLocalPastWorkouts(it)
                return TrainResponse(data = it)
            } ?: resp.code?.let { code ->
                return TrainResponse(code = code, message = resp.message)
            }
        }

        return TrainResponse(data = localPastWorkouts)
    }

    override suspend fun saveTraining(newTrain: Training) {
        repository.save(newTrain)

        store.setLocalPastWorkouts(null)
        store.setLocalWorkouts(null)
        store.setLocalCurrentWorkout(null)
    }

    override suspend fun startTraining(uid: String) {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        repository.startTraining(uid)

        store.setLocalPastWorkouts(null)
        store.setLocalWorkouts(null)
        store.setLocalCurrentWorkout(null)
    }

    override suspend fun finishTraining(uid: String) {
        repository.finishTraining(uid)
    }

    override suspend fun endTraining() {
        store.setLocalCurrentExercise(null)
        store.setLocalCurrentExerciseSets(null)

        store.setLocalCurrentWorkout(null)
        store.setLocalPastWorkouts(null)
        store.setLocalWorkouts(null)
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

        store.setLocalPastWorkouts(null)
        store.setLocalWorkouts(null)
        store.setLocalCurrentWorkout(null)

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

    override suspend fun clearLocalExerciseData() {
        store.clearLocalExerciseData()
    }

    override suspend fun saveLocalExercise(exercise: TrainingExercise) {
        store.saveLocalEx(exercise)
    }

    override suspend fun getAllLocalExercise(): List<TrainingExercise> {
        return store.getAllLocalEx()
    }

    override suspend fun getLocalTrainingByUid(uid: String): Training? {
        return store.getLocalTrainingByUid(uid)
    }

    override suspend fun setLocalExerciseAndSets(exerciseAndSets: LocalExerciseAndSets) {
        store.setLocalCurrentExerciseSets(exerciseAndSets.sets)
        store.setLocalCurrentExercise(exerciseAndSets.indexExercise)
    }

}