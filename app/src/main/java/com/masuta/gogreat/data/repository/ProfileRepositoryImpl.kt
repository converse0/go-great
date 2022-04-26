package com.masuta.gogreat.data.repository

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.ProfileRepository
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.response.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
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

@Serializable
data class ResponseParamsIm(
    val message: String?= null,
    val status: Boolean?= null,
    val code: Int?= null,
    val data: String? = null
)

class ProfileRepositoryImpl @Inject constructor(
    private val client: Client,
): ProfileRepository {

    private var profileParams = mutableStateOf<ParametersUser?>(null)

    override var isLoadData: Boolean = true

    override suspend fun createParameters(params: ParametersUserSet): String {
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

    private fun getStringRandom(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuilder(7)
        for (i in 0 until 7) {
            val randomChar = chars[random.nextInt(chars.length)]
            sb.append(randomChar)
        }
        return sb.toString()
    }

    private fun imageBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override suspend fun uploadImage(image: ImageBitmap): ResponseParamsIm {

    val convertedImage = imageBitmapToByteArray(image.asAndroidBitmap())

        println("userToken: $userToken")
        println("refreshToken: $refreshUserToken")
        println("image: ${convertedImage.size / 1024}")
        val imageName = getStringRandom()
        val size = convertedImage.size / 1024
        if (size > 3 * 1024) {
            return ResponseParamsIm(
                message = "Image size is too big",
                status = false,
                code = 400
            )
        }
        val response = client.makeClient()
            .post<ResponseParamsIm>("https://boilerplate-go-trening.herokuapp.com/v1/files") {
//                contentType(ContentType.Application.Json)
                headers {
//                    append("Content-Type", ContentType.Application.Json)
                    append("Authorization", "Bearer $userToken")
                }
                body = MultiPartFormDataContent(
                    formData {
                        this.append(FormPart("bitmapName", imageName))
                        this.appendInput(
                            key = "attachment",
                            headers = Headers.build {
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=$imageName"
                                )
                            },
                        ) { buildPacket { writeFully(convertedImage) } }}
                )
            }
        println("Response IMAGE: $response")
    return  response
    }

    override suspend fun getLocalProfileParams(): ParametersUser? {
        return profileParams.value
    }

    override suspend fun setLocalProfileParams(params: ParametersUser) {
        profileParams.value = params
    }

}