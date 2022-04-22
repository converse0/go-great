package com.masuta.gogreat.data.repository

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.ProfileRepository
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UserProfileResp(val id: String,
                           val email: String,
                           val username: String,
                           var isverified: Boolean)

@Serializable
data class ResponseProf(
    val message: String? = null,
    val status: Boolean? = null,
    val data: UserProfileResp? = null
)

@Serializable
data class ResponseParams(
    val message: String?= null,
    val status: Boolean?= null,
    val code: Int?= null,
    val data: ParametersUserGet? = null
)

class ProfileRepositoryImpl @Inject constructor(
    private val client: Client
): ProfileRepository {
    override suspend fun createParameters(params: ParametersUserSet): String {
        println("accessToken: $userToken")
        println("refreshToken: $refreshUserToken")
        println("params: $params")
        val response = client.makeClient()
            .post<String>("https://boilerplate-go-trening.herokuapp.com/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = params
            }
        return response
    }

    override suspend fun getParameters(): ResponseParams {
        println("userToken: $userToken")
        userToken?.let {
            val response = client.makeClient().get<ResponseParams>(
                "https://boilerplate-go-trening.herokuapp.com/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${userToken}")
                }
            }
            println("getParameters: $response")
            return response
        } ?: return ResponseParams()

    }

    override suspend fun updateParameters(params: ParametersUserSet): String {
        println("userToken: $userToken")
        println("refreshToken: $refreshUserToken")
        println("params: $params")
        val response = client.makeClient()
            .put<UpdateParamsResponse>("https://boilerplate-go-trening.herokuapp.com/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = params
            }
        println(response)

        return response.message
    }
}