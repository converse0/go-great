package com.masuta.gogreat.core.providers

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.masuta.gogreat.R
import com.masuta.gogreat.core.http.Client
import com.masuta.gogreat.core.model.*
import com.masuta.gogreat.utils.imageBitmapToByteArray
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileImpl @Inject constructor(
    private val client: Client,
    private val context: Context
): Profile {

    private var trainUrl = ""
    private var httpClient: HttpClient? = null
    private val megabyte = 1024
    private var maxImageLimit = 0

    init {
        context.resources.getInteger(R.integer.request_timeout).let {
            httpClient = client.makeClient(it.toLong())
        }
        context.getString(R.string.train_url).let {
            if (it.isNotEmpty()) {
                trainUrl = it
            }
        }
        context.resources.getInteger(R.integer.max_image_size).let {
            maxImageLimit = it * megabyte
        }
    }

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

    override suspend fun updateParameters(params: ParametersUserSet): UpdateParamsResponse {
        try {
            httpClient?.put<UpdateParamsResponse>("$trainUrl/user/parameters") {
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
        return UpdateParamsResponse()
    }

    override suspend fun uploadImage(image: ImageBitmap): ResponseParamsIm {
        val convertedImage = imageBitmapToByteArray(image.asAndroidBitmap())

        val imageName = "image3.jpg"
        val imageSizeInKB = convertedImage.size / megabyte
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
}