package com.bed.android.bedrock.kakaovision

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface KakaoApi {
    @Multipart
    @Headers(
        "Authorization: ${OCRConstants.AUTH_HEADER}"
    )
    @POST("/v2/vision/text/ocr")
    fun getOCR(@Part image : MultipartBody.Part) : Call<OCRResponse>
}