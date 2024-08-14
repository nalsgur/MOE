package com.example.moe.search.remote


import com.example.moe.follow.entities.Follow
import com.example.moe.detail.search.entities.Search
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface SearchRetrofitInterface {

    @GET("/search/{userId}")
    suspend fun getSearchData(@Path(value = "userId") userId : Int, @Query("keyword") keyword: String, @Query("page") page: Int = 0): Response<List<Search>>


    @GET("/search/recent/{userId}")
    suspend fun getRecentSearchData(@Path(value= "userId") userId: Int): Response<List<Search>>

    @POST("/follows/toggleHeart/{followId}")
    suspend fun setLike(@Path("followId")followId: Int): Response<Follow>
}

