package com.example.moe.login.loginManager

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginApi {
    @GET("/oauth2/callback/{provider}")
    fun getJwtToken(
        @Path("provider") provider: String,
        @Query("accessToken") accessToken:String
    ): Call<JwtResponse>
}

data class JwtResponse(val jwtToken: String)