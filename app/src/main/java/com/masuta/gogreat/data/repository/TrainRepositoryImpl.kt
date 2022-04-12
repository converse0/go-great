package com.masuta.gogreat.data.repository

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class TrainRepositoryImpl @Inject constructor(
    private val client: Client
): TrainRepository {
    private var httpClient: HttpClient? = null
    private val url = "https://boilerplate-go-trening.herokuapp.com"
    private val token = userToken
    private var localTraining:Map<Int,Training> = mutableMapOf()
    private var localTrainingEx:Map<Int,TrainingExercise> = mutableMapOf()

    init {
       httpClient = client.makeClient()
    }
    override suspend fun findAll(): TrainingResponse {
        httpClient?.get<TrainingResponse>("$url/user/trenings") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $token")
            }
        }.let {
            return it!!
        }
    }

    override suspend fun findById(id: Long): ExerciseResponse {
        ExerciseType.values()[id.toInt()].let {
            return httpClient?.get<ExerciseResponse>(
                "$url/user/exercises/default?type=${id.toString().lowercase()}") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $token")
                }
            }!!
        }
    }

    override suspend fun save(newTrain: Training) {
      val resp = httpClient?.post<String>("$url/user/trening") {
          contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $token")
            }
            body = newTrain
        }
        println(resp)

    }

    override suspend fun saveLocal(newTrain: Training):Int {
        val id = localTraining.size.plus(1)
        localTraining = localTraining.plus(id to newTrain)
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

    override fun delete(newTrain: Training) {
        TODO("Not yet implemented")
    }
}
