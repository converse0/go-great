package com.masuta.gogreat.data.providers

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.masuta.gogreat.R
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.data.store.Store
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrainRepositoryImpl @Inject constructor(
    private var client: Client,
    private val context: Context,
    private val store: Store
): TrainRepository {

    private var httpClient: HttpClient? = null
    private var trainUrl = ""
    private var localTraining:Map<String,Training> = mutableMapOf()
    private var localTrainingEx:Map<Int,TrainingExercise> = mutableMapOf()

    private var localWorkouts: List<Training> = mutableListOf()
    private var localCurrentWorkout = mutableStateOf<Training?>(null)
    private var localPastWorkouts: List<Training> = mutableListOf()

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
        try {
            httpClient?.get<TrainingResponse>("$trainUrl/user/trenings") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }.let {
                return it!!
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return TrainingResponse()
    }

    override suspend fun findById(id: Long): ExerciseResponse {
        try {
            ExerciseType.values()[id.toInt()].let {
                val type = it.toString().lowercase()
                val resp = httpClient?.get<ExerciseResponse>(
                    "$trainUrl/user/exercises/default?type=${type}") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append("Authorization", "Bearer $userToken")
                    }
                }!!
                println("RESPONSE FIND BY ID: $resp")
                return resp
            }
        } catch (e: Exception) {
            println("EXCEPTION: $e")
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
            return ExerciseResponse(code = 777)
        }
    }

    override suspend fun save(newTrain: Training) {
        try {
           val resp =  httpClient?.post<String>("$trainUrl/user/trening") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = newTrain
            }
            println("save resp: $resp")
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
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

    override suspend fun getPassTrainings(): TrainingResponse {
        try {
            httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Finish") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }?.let { tr ->
                println("TRAINING RESPONSE $tr")
                return tr
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return TrainingResponse()
    }

    override suspend fun getMyTrainings(): TrainingResponse {
        try {
            httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Create") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }?.let { tr ->
                return tr
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return TrainingResponse()
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
        return store.getLocalCurrentExercise()
    }

    override suspend fun setLocalCurrentExercise(indexExercise: Int?) {
        store.setLocalCurrentExercise(indexExercise)
    }

    override suspend fun getLocalCurrentExerciseSets(): Int? {
        return store.getLocalCurrentExerciseSets()
    }

    override suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?) {
        store.setLocalCurrentExerciseSets(exerciseSets)
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

    override suspend fun getTrainingDetail(uid: String): Training? {
        try {
            httpClient?.get<TrainingDetailsResponse>("$trainUrl/user/trening?uid=$uid") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }.let {
                return it!!.data!!
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return null
    }

    override suspend fun startTraining(uid: String) {
        try {
            httpClient?.put<String>("$trainUrl/user/trening/status") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = mapOf("uid" to uid, "status" to "Start")
            }?.let {
                println("startTraining: $it")
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
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
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
    }


    override suspend fun getCurrentTraining(): TrainingResponse {
        try {
            httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Start") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }?.let { tr ->
                return tr
//                tr.data?.let { train ->
//                    train.getOrNull(0)?.let {
//                        return it
//                    }
//                }
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return TrainingResponse()
    }

    override suspend fun setExerciseParams(uid: String, listExercises: List<TrainingExercise>) {
        val data = TrainingExerciseUpdate(uid=uid, exercises = listExercises)
        try {
            httpClient?.put<String>("$trainUrl/user/trening/exercises") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = data
            }?.let {
                println("setExerciseParams: $it")
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
    }
}
