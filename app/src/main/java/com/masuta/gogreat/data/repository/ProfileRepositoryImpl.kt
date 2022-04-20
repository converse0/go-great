package com.masuta.gogreat.data.repository

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.ParametersUserGet
import com.masuta.gogreat.domain.model.ParametersUserSet
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
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

    override suspend fun getParameters(): Pair<ParametersUserGet?, String?> {
        println("userToken: $userToken")
        val response = client.makeClient().get<ResponseParams>(
            "https://boilerplate-go-trening.herokuapp.com/user/parameters") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer ${userToken}")
            }
        }
        println("getParameters: $response")

        if (response.status == null) {
            return Pair(null, response.message)
        }
        return Pair(response.data, null)
    }

    override suspend fun updateParameters(params: ParametersUserSet): String {
        println("userToken: $userToken")
        println("refreshToken: $refreshUserToken")
        println("params: $params")
        val response = client.makeClient()
            .put<String>("https://boilerplate-go-trening.herokuapp.com/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = params
            }
        println(response)
        return response
    }

}