package com.masuta.gogreat.domain.use_case

import com.masuta.gogreat.domain.model.ParametersUser
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class ProfileUseCase {
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
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }
        }
    }
    /** do http request post to url user/params */
    suspend fun updateParameters(params: ParametersUser): String {
        val client = makeClient()
        val response = client
            .post<String>("https://gogreat.masuta.com/user/parameters") {
            contentType(ContentType.Application.Json)
            body = params
        }
        return response
    }
}