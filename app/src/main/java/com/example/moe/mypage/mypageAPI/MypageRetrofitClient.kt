package com.example.moe.mypage.mypageAPI

import com.example.moe.MainAPI.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MypageRetrofitClient {
    private const val BASE_URL = "https://umc.memoryofexhibition.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: MypageApiService = retrofit.create(MypageApiService::class.java)
}
