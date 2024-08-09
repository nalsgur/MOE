package com.example.moe.login.loginManager

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DefaultLoginApi {
    @POST("/user/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
}