package com.masuta.gogreat.domain.use_case

import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

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
    val data: ParametersUser? = null
)


class ProfileUseCase {

    private fun getToken() {

    }

    private fun makeClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = false

            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
//            install(Auth) {
//                bearer {
//                    BearerTokens(accessToken = userToken!!, refreshToken = refreshUserToken!!)
//                }
//                // Configure authentication
//            }
        }
    }
    /** do http request post to url user/params */
    suspend fun createParameters(params: ParametersUser): String {
        println("accessToken: ${userToken}")
        println("refreshToken: ${refreshUserToken}")
        val client = makeClient()
        val response = client
            .post<String>("https://boilerplate-go-trening.herokuapp.com/user/parameters") {
            contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${userToken}")
                }
            body = params
        }
        return response
    }
    /** do http request get to url user/parameters */
    suspend fun getParameters(): ParametersUser? {
        val client = makeClient()

        val prof = client
            .get<ResponseProf>("https://boilerplate-go.herokuapp.com/profile") {
            contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${userToken}")
                }
        }
        println("prof: ${prof}")
        if (prof.status == null) {
            return null
        }
        println("userToken: ${userToken}")
        println(prof.data!!.id)
        val response = client.get<ResponseParams>(
            "https://boilerplate-go-trening.herokuapp.com/user/parameters?id=${prof.data!!.id}") {
            contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${userToken}")
                }
        }
        println(response)
        if (response.status == null) {
            return null
        }
        return response.data!!
    }
}