package com.bed.android.bedrock.kakaovision

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OCRConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val kakaoApi: KakaoApi by lazy {
        retrofit.create(KakaoApi::class.java)
    }

}