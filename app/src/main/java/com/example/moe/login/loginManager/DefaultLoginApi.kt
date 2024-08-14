package com.example.moe.login.loginManager

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DefaultLoginApi {
    @POST("/user/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/user/logout")
    fun logoutUser(@Header("Authorization") token: String): Call<Void>
}