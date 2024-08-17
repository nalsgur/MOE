package com.example.moe.login.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupApiService {
    @POST("/user/join")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}