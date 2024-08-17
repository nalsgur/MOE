package com.example.moe.login.signup

import com.example.moe.MainAPI.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SignupRetrofitClient {
    private const val BASE_URL = "https://umc.memoryofexhibition.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: SignupApiService = retrofit.create(SignupApiService::class.java)
}