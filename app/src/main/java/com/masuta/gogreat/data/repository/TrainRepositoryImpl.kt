package com.masuta.gogreat.data.repository

import androidx.compose.runtime.mutableStateOf
import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
const val url = "https://api.gogreat.com/v1/train"

class TrainRepositoryImpl @Inject constructor(
    client: Client,
): TrainRepository {

    private var httpClient: HttpClient? = null
    private val url = "https://boilerplate-go-trening.herokuapp.com"
    private var localTraining:Map<String,Training> = mutableMapOf()
    private var localTrainingEx:Map<Int,TrainingExercise> = mutableMapOf()

    private var localWorkouts: List<Training> = mutableListOf()
    private var localCurrentWorkout = mutableStateOf<Training?>(null)
    private var localPastWorkouts: List<Training> = mutableListOf()

    override var workoutsDataReload: Boolean = true
    override var pastWorkoutsDataReload: Boolean = true
    override var currentWorkoutDataReload: Boolean = true

    init {
       httpClient = client.makeClient()
    }

    override suspend fun findAll(): TrainingResponse {
        httpClient?.get<TrainingResponse>("$url/user/trenings") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }.let {
            return it!!
        }
    }

    override suspend fun findById(id: Long): ExerciseResponse {
        ExerciseType.values()[id.toInt()].let {
            val type = it.toString().lowercase()
            val resp = httpClient?.get<ExerciseResponse>(
                "$url/user/exercises/default?type=${type}") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }!!
            return resp
        }
    }


    override suspend fun save(newTrain: Training) {
      val resp = httpClient?.post<String>("$url/user/trening") {
          contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
            body = newTrain
        }
        saveLocal(newTrain)

    }

    override suspend fun saveLocal(newTrain: Training): String {
        val id = newTrain.uid ?: ""
        localTraining.get(id)?.apply {
            this.image = newTrain.image
            this.uid = id
            this.name= newTrain.name
            this.exercises = newTrain.exercises

        } ?: run {
            localTraining = localTraining.plus(id to newTrain)
        }
        return id
    }

    override suspend fun saveLocalEx(ex: TrainingExercise): Int {
        val id = localTrainingEx.size.plus(1)
        localTrainingEx = localTrainingEx.plus(id to ex)
        return id
    }

    override suspend fun getLocalEx(id: Int): TrainingExercise? {
        localTrainingEx.get(id).let {
            return it
        }
    }

    override suspend fun getAllLocalTrainings(): List<Training>? {
        return if (localTraining.isNotEmpty()) {
            localTraining.values.toList()
        } else {
            null
        }
    }



    override suspend fun getPassTrainings(): List<Training>? {
        httpClient?.get<TrainingResponse>("$url/user/trenings?status=Finish") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->

            tr.data?.let { trains ->
                return trains
            }
        }
        return null
    }

    override suspend fun getMyTrainings(): List<Training>? {
        httpClient?.get<TrainingResponse>("$url/user/trenings?status=Create") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->
            tr.data?.let { trains ->
                return trains
            }
        }
        return null
    }

    override suspend fun getLocalWorkouts(): List<Training> {
        return localWorkouts
    }

    override suspend fun setLocalWorkouts(workouts: List<Training>) {
        localWorkouts = workouts
    }

    override suspend fun getLocalCurrentWorkout(): Training? {
        return localCurrentWorkout.value
    }

    override suspend fun setLocalCurrentWorkout(workout: Training) {
        localCurrentWorkout.value = workout
    }

    override suspend fun getLocalPastWorkouts(): List<Training> {
        return localPastWorkouts
    }

    override suspend fun setLocalPastWorkouts(workouts: List<Training>) {
        localPastWorkouts = workouts
    }

    override suspend fun getLocalTrainingByUid(uid: String): Training? {
        localTraining.get(uid).let {
            return it
        }
    }

    override suspend fun getAllLocalEx(): List<TrainingExercise> {
        return localTrainingEx.values.toList()
    }

    override suspend fun clearLocalExerciseData() {
        localTrainingEx = mutableMapOf()
    }
    override suspend fun clearLocalTrainingData() {
        localTraining = mutableMapOf()
    }

    override suspend fun getTrainingDetail(uid: String): Training {
        httpClient?.get<TrainingDetailsResponse>("$url/user/trening?uid=$uid") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }.let {
            return it!!.data!!
        }

    }

    override suspend fun startTraining(uid: String) {
        httpClient?.put<String>("$url/user/trening/status") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
            body =  mapOf("uid" to uid, "status" to "Start")
        }?.let {
            println("startTraining: $it")
        }
    }

    override suspend fun finishTraining(uid: String) {
        httpClient?.put<String>("$url/user/trening/status") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
            body =  mapOf("uid" to uid, "status" to "Finish")
        }?.let {
            println("finishTraining: $it")
        }
    }


    override suspend fun getCurrentTraining(): Training? {
        httpClient?.get<TrainingResponse>("$url/user/trenings?status=Start") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->

            tr.data?.let { train ->
                train.getOrNull(0)?.let {
                    return it
                }
            }
        }
        return null
    }


    override suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        val data = TrainingExerciseUpdate(uid=uid, exercises = listExercises)
        httpClient?.put<String>("$url/user/trening/exercises") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
            body = data // mapOf("uid" to uid, "exercises" to listExercises)
        }?.let {
            println("setExerciseParams: $it")
        }
    }

    override fun delete(newTrain: Training) {
        TODO("Not yet implemented")
    }
}
