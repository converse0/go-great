package com.masuta.gogreat.data.repository

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.TrainRepository
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class TrainRepositoryImpl @Inject constructor(
    private val client: Client
): TrainRepository {
    private var httpClient: HttpClient? = null
    private val url = "https://boilerplate-go-trening.herokuapp.com"
    private val token = userToken
    init {
       httpClient = client.makeClient()
    }
    override suspend fun findAll(): TrainingResponse {
        httpClient?.get<TrainingResponse>("$url/user/trainings") {
            header("Content-Type", "application/json")
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
                header("Content-Type", "application/json")
                headers {
                    append("Authorization", "Bearer $token")
                }
            }!!
        }
    }

    override suspend fun save(newTrain: Training) {
      httpClient?.post<TrainingResponse>("$url/user/trainings") {
            header("Content-Type", "application/json")
            headers {
                append("Authorization", "Bearer $token")
            }
            body = newTrain
        }
    }

    override fun delete(newTrain: Training) {
        TODO("Not yet implemented")
    }
}
