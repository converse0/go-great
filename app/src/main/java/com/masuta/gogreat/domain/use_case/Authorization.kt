package com.masuta.gogreat.domain.use_case

import com.masuta.gogreat.domain.model.User
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class Authorization {
    fun isAuthorized(): Boolean {
        return true
    }

    fun makeClient(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }
        }
    }

    /** do http post request /login */
    suspend fun login(user:User): Boolean {
        val client = makeClient()

        val response = client.post<String>("https://boilerplate-go.herokuapp.com/login") {
            contentType(ContentType.Application.Json)
            body = user
        }
        println(response)
        return true
    }
}