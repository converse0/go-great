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
                append("Authorization", "Bearer $userToken")
            }
        }.let {
            println("findAll: $it")
            return it!!
        }
    }

    override suspend fun findById(id: Long): ExerciseResponse {
        ExerciseType.values()[id.toInt()].let {
            val type = it.toString().lowercase()
            println(type)
            val resp = httpClient?.get<ExerciseResponse>(
                "$url/user/exercises/default?type=${type}") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
            }!!
            println(   "findById: $resp" )
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
        println("save: $resp")

    }

    override suspend fun saveLocal(newTrain: Training):Int {
        val id = localTraining.size.plus(1)
        localTraining = localTraining.plus(id to newTrain)
        return id
    }

    override suspend fun saveLocalEx(ex: TrainingExercise): Int {
        val id = localTrainingEx.size.plus(1)
        localTrainingEx = localTrainingEx.plus(id to ex)
        println(localTrainingEx)
        return id
    }

    override suspend fun getLocalEx(id: Int): TrainingExercise? {
        localTrainingEx.get(id).let {
            return it
        }
    }

    override suspend fun getAllLocalEx(): List<TrainingExercise> {
        return localTrainingEx.values.toList()
    }

    override suspend fun clearLocalExerciseData() {
        localTrainingEx = mutableMapOf()
    }

    override suspend fun getTrainingDetail(uid: String): Training {
        httpClient?.get<TrainingDetailsResponse>("$url/user/trening?uid=$uid") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }.let {
            println("getTrainingDetail: $it")
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

    override suspend fun getCurrentTraining(): Training? {
        httpClient?.get<TrainingResponse>("$url/user/trenings?status=Start") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $userToken")
            }
        }?.let { tr ->
            println("getCurrentTraining: $tr")
            tr.data?.let { train ->
                train.getOrNull(0)?.let {
                    return it
                }
            }
        }
        return null
    }


    override fun delete(newTrain: Training) {
        TODO("Not yet implemented")
    }
}
