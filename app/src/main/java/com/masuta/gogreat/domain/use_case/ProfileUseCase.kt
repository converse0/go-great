package com.masuta.gogreat.domain.use_case

import com.masuta.gogreat.domain.model.ParametersUser
import com.masuta.gogreat.domain.model.refreshUserToken
import com.masuta.gogreat.domain.model.userToken
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

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
    suspend fun updateParameters(params: ParametersUser): String {
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
}