package com.example.moe

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object RetrofitInstance{

    const val BASE_URL = "https://umc.memoryofexhibition.com/"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance(): Retrofit {
        return retrofit
    }
}

interface RetrofitInterface {

    @GET("/search/{userId}")
    suspend fun getSearchData(@Path(value = "userId") userId : Int, @Query("keyword") keyword: String, @Query("page") page: Int = 0): List<Search>


    @GET("/search/recent/{userId}")
    suspend fun getRecentSearchData(@Path(value= "userId") userId: Int): List<Search>
}