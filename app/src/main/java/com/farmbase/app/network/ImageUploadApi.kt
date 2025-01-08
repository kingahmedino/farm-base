package com.farmbase.app.network

import com.farmbase.app.BuildConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ImageUploadApi {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("api_key") apiKey: RequestBody
    ): Response<ImageUploadResponse>
}

data class ImageUploadResponse(
    val success: Boolean,
    val status: Int,
    val message: String,
    val data: ImageData
)

data class ImageData(
    val title: String,
    val url: String,
    val viewUrl: String,
    val extension: String,
    val size: Int,
    val createdAt: String
)