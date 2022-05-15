package com.masuta.gogreat.core.providers

import android.content.Context
import android.widget.Toast
import com.masuta.gogreat.R
import com.masuta.gogreat.core.http.Client
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
): TrainRepository {

    private var httpClient: HttpClient? = null
    private var trainUrl = ""

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
                return resp
            }
        } catch (e: Exception) {
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
           httpClient?.post<String>("$trainUrl/user/trening") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = newTrain
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

    override suspend fun getPassTrainings(): TrainingResponse {
        try {
            httpClient?.get<TrainingResponse>("$trainUrl/user/trenings?status=Finish") {
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
