package com.masuta.gogreat.data.providers

import android.content.Context
import android.widget.Toast
import com.masuta.gogreat.R
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.domain.model.Response
import com.masuta.gogreat.domain.model.User
import com.masuta.gogreat.domain.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: Client,
    private val context: Context
) : AuthRepository {
    private var httpClient: HttpClient? = null
    private var authUrl = ""

    init {
        context.resources.getInteger(R.integer.request_timeout).let {
            httpClient = client.makeClient(it.toLong())
        }
        context.getString(R.string.auth_url).let {
            if (it.isNotEmpty()) {
                authUrl = it
            }
        }
    }

    override suspend fun login(user: User): Map<String, Any?> {
        try {
            val response = httpClient?.post<Response>("$authUrl/login") {
                contentType(ContentType.Application.Json)
                body = user
            }
            response?.let { res ->
                res.data?.let {
                    return mapOf<String,Any?>(
                        "status" to res.status,
                        "message" to res.message,
                        "loginResponse" to it
                    )
                }
                return mapOf<String,Any?>(
                    "status" to res.status,
                    "message" to res.message,
                    "loginResponse" to null
                )
            }
        } catch(e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return mapOf<String,Any?>(
            "status" to null,
            "message" to null,
            "loginResponse" to null
        )
    }

    override suspend fun signup(user: User): Boolean {
        try {
            val response = httpClient?.post<HttpResponse>("$authUrl/signup") {
                contentType(ContentType.Application.Json)
                body = user
            }
            response?.let { res ->
                if (res.status.value==201) {
                    return true
                }
            }
            return false
        } catch(e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return false
    }
}