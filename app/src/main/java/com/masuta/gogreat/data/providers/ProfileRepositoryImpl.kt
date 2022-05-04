package com.masuta.gogreat.data.providers

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.masuta.gogreat.R
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.domain.model.*
import com.masuta.gogreat.domain.repository.ProfileRepository
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