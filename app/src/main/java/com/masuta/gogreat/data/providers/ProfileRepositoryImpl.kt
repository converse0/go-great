package com.masuta.gogreat.data.providers

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.masuta.gogreat.R
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.ProfileRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

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
    private val context: Context
): ProfileRepository {

    private var trainUrl = ""
    private var httpClient: HttpClient? = null
    private val kilobyte = 1024
    private val maxImageLimit = 3 * kilobyte

    init {
        context.resources.getInteger(R.integer.request_timeout).let {
            httpClient = client.makeClient(it.toLong())
        }
        context.getString(R.string.train_url).let {
            if (it.isNotEmpty()) {
                trainUrl = it
            }
        }
    }

    private var profileParams = mutableStateOf<ParametersUser?>(null)

    override var isLoadData: Boolean = true

    override suspend fun createParameters(params: ParametersUserSet): String? {
        try {
            httpClient?.post<String>("$trainUrl/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = params
            }?.let {
                return it
            }
        } catch(e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getParameters(): ResponseParams {
        try {
            userToken?.let {
                httpClient?.get<ResponseParams>("$trainUrl/user/parameters") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append("Authorization", "Bearer ${userToken}")
                    }
                }?.let {
                    println("getParameters: $it")
                    return it
                }
            } ?: return ResponseParams()
        } catch (e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
            return ResponseParams(code = 777)
        }
    }

    override suspend fun updateParameters(params: ParametersUserSet): String? {
        try {
            httpClient?.put<UpdateParamsResponse>("$trainUrl/user/parameters") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $userToken")
                }
                body = params
            }?.let {
                return it.message
            }
        } catch(e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
        }
        return null
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
        println("image: ${convertedImage.size / kilobyte}")
        val imageName = "image3.jpg"
        val imageSizeInKB = convertedImage.size / kilobyte
        if (imageSizeInKB > maxImageLimit) {
            return ResponseParamsIm(
                message = "Image size is too big",
                status = false,
                code = 400
            )
        }
        try {
           httpClient?.post<ResponseParamsIm>("$trainUrl/v1/files") {

                headers {
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
            }?.let {
                println("Response IMAGE: $it")
                return it
            }
        } catch(e: Exception) {
            e.localizedMessage?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
            e.printStackTrace()
            return ResponseParamsIm(code = 777)
        }
        return ResponseParamsIm()
    }

    override suspend fun getLocalProfileParams(): ParametersUser? {
        return profileParams.value
    }

    override suspend fun setLocalProfileParams(params: ParametersUser) {
        profileParams.value = params

    }

}