package com.masuta.gogreat.data.repository

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.Response
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: Client
) : AuthRepository {

    override suspend fun login(user: User): Map<String, Any?> {
        val response: Response = client.makeClient()
            .post("https://boilerplate-go.herokuapp.com/login") {
                contentType(ContentType.Application.Json)
                body = user
            }

        response.data?.let {
            println(it.accessToken)

            return mapOf<String,Any?>(
                "status" to response.status,
                "message" to response.message,
                "loginResponse" to it
            )
        }
        return mapOf<String,Any?>(
            "status" to response.status,
            "message" to response.message,
            "loginResponse" to null
        )
    }

    override suspend fun signup(user: User): Boolean {
        val response: HttpResponse = client.makeClient().post("https://boilerplate-go.herokuapp.com/signup") {
            contentType(ContentType.Application.Json)
            body = user
        }
        println(response.status.description)
        if (response.status.value==201) {
            return true
        }
        return false
    }
}