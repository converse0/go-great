package com.masuta.gogreat.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.bumptech.glide.load.HttpException
import com.masuta.gogreat.R
import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import javax.inject.Inject

class TrainRepositoryImpl @Inject constructor(
    private var client: Client,
    private var context: Context,
): TrainRepository {
    private var httpClient: HttpClient? = null
    private var trainUrl = "https://api.gogreat.com/v1/train"
    private var localTraining:Map<String,Training> = mutableMapOf()
    private var localTrainingEx:Map<Int,TrainingExercise> = mutableMapOf()

    private var localWorkouts: List<Training> = mutableListOf()
    private var localCurrentWorkout = mutableStateOf<Training?>(null)
    private var localPastWorkouts: List<Training> = mutableListOf()

//    private var localCurrentExercise = mutableStateOf<Int?>(null)
//    private var localCurrentExerciseSets = mutableStateOf<Int?>(null)

    override var workoutsDataReload: Boolean = true
    override var pastWorkoutsDataReload: Boolean = true
    override var currentWorkoutDataReload: Boolean = true

    init {

        context.resources.getInteger(R.integer.request_timeout).let {
            httpClient = client.makeClient(it.toLong())
        }
        context.getString(R.string.train_url).let {
            if (it.isNotEmpty()) {
                trainUrl = it
            }
        }
    }

    override suspend fun findAll(): TrainingResponse {
        httpClient?.get<TrainingResponse>("$trainUrl/user/trenings") {
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
                "$trainUrl/user/exercises/default?type=${type}") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }!!
            return resp
        }
    }


    override suspend fun save(newTrain: Training) {
      val resp = httpClient?.post<String>("$trainUrl/user/trening") {
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
        try {
        httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Finish") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->

            tr.data?.let { trains ->
                return trains
            }
        }
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        catch (e: ConnectTimeoutException) {
            e.printStackTrace()
        }
        catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getMyTrainings(): List<Training>? {
        try{
        httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Create") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->
            tr.data?.let { trains ->
                return trains
            }
        }
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        catch (e: ConnectTimeoutException) {
            e.printStackTrace()
        }
        catch (e: SocketTimeoutException) {
            e.printStackTrace()
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

    override suspend fun setLocalCurrentWorkout(workout: Training?) {
        localCurrentWorkout.value = workout
    }

    override suspend fun getLocalPastWorkouts(): List<Training> {
        return localPastWorkouts
    }

    override suspend fun setLocalPastWorkouts(workouts: List<Training>) {
        localPastWorkouts = workouts
    }

    override suspend fun getLocalCurrentExercise(): Int? {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val value = sharedPref.getInt("currentExercise", -1)
        println("LOCAL EXERCISE: ${value}")
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExercise(indexExercise: Int?) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("currentExercise", indexExercise ?: -1)
        editor.apply()
    }

    override suspend fun getLocalCurrentExerciseSets(): Int? {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val value = sharedPref.getInt("currentExerciseSets", -1)
        println("LOCAL SETS: ${value}")
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("currentExerciseSets", exerciseSets ?: -1)
        editor.apply()
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

        httpClient?.get<TrainingDetailsResponse>("$trainUrl/user/trening?uid=$uid") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }.let {
            return it!!.data!!
        }

    }

    override suspend fun startTraining(uid: String) {
        httpClient?.put<String>("$trainUrl/user/trening/status") {
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
        try {


        httpClient?.put<String>("$trainUrl/user/trening/status") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
            body =  mapOf("uid" to uid, "status" to "Finish")
        }?.let {
            println("finishTraining: $it")
        }

        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        catch (e: ConnectTimeoutException) {
            e.printStackTrace()
        }
        catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }


    override suspend fun getCurrentTraining(): Training? {
        try {
        httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Start") {
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
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        catch (e: ConnectTimeoutException) {
            e.printStackTrace()
        }
        catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
        return null
    }


    override suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        val data = TrainingExerciseUpdate(uid=uid, exercises = listExercises)
        try {
            httpClient?.put<String>("$trainUrl/user/trening/exercises") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = data // mapOf("uid" to uid, "exercises" to listExercises)
            }?.let {
                println("setExerciseParams: $it")
            }
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        catch (e: ConnectTimeoutException) {
            e.printStackTrace()
        }
        catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }


    }

    override fun delete(newTrain: Training) {
        TODO("Not yet implemented")
    }
}
