package com.masuta.gogreat.domain.use_case

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.masuta.gogreat.domain.model.LoginResponse
import com.masuta.gogreat.domain.model.Response
import com.masuta.gogreat.domain.model.User
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class Authorization {


    fun isAuthorized(): Boolean {
        return true
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
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }
        }
    }

    /** do http post request /login */
    suspend fun login(user:User): Pair<Boolean, String> {
        val client = makeClient()

        val response: Response = client
            .post("https://boilerplate-go.herokuapp.com/login") {
            contentType(ContentType.Application.Json)
            body = user
        }
        response.data?.let {
            println(it.accessToken)

            return Pair(true, it.accessToken)
        }
        return Pair(false, "")
    }

    /** do http post request /signup */
    suspend fun signup(user:User): Boolean {
        val client = makeClient()

        val response:HttpResponse = client.post("https://boilerplate-go.herokuapp.com/signup") {
            contentType(ContentType.Application.Json)
            body = user
        }
        if (response.status.value==201) {
            return true
        }
        return false
    }
}