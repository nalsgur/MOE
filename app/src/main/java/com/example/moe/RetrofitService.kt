package com.example.moe

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class mypageServer (
    val name : String
)

class changeName(
    val name : String
)

interface RetrofitService {
    @GET("/user/my-page/search/")
    fun getName() : Call<ArrayList<mypageServer>>

    @POST("/user/my-page/change-nickname/")
    fun changeName(
        @Body params : HashMap<String, Any>
    ) : Call<changeName>
}