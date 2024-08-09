package com.example.moe

import com.example.moe.record.Memo
import com.example.moe.record.Photo
import com.example.moe.record.Record
import com.example.moe.record.RecordPage
import com.example.moe.record.RecordPhoto
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


object RetrofitInstance{

    private const val BASE_URL = "https://umc.memoryofexhibition.com/"

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
    suspend fun getSearchData(@Path(value = "userId") userId : Int, @Query("keyword") keyword: String, @Query("page") page: Int = 0): Response<List<Search>>


    @GET("/search/recent/{userId}")
    suspend fun getRecentSearchData(@Path(value= "userId") userId: Int): Response<List<Search>>

    @POST("/add/{recordPageId}/recordPhoto")
    fun uploadPhoto(@Path("recordPageId") recordPageId: Int, @Body photoUrl: Photo): Call<Void>

    @POST("/record/{recordPageId}/{recordPhotoId}")
    fun record(@Path("recordPageId") recordPageId: Int, @Path("recordPhotoId")recordPhotoId: Int, @Body body: Memo): Call<Void>

    @GET("/record/{userId}/latest")
    suspend fun getRecordLatest(@Path("userId")userId: Int, @Query("pageNumber")pageNumber: Int): Response<List<Record>>

    @GET("/record/{userId}/oldest")
    suspend fun getRecordOldest(@Path("userId")userId: Int, @Query("pageNumber")pageNumber: Int): Response<List<Record>>

    @GET("/record/{userId}/{recordPageId}")
    suspend fun getRecordPage(@Path("userId")userId: Int, @Path("recordPageId")recordPageId: Int): Response<RecordPage>

    @GET("/record/{userId}/{recordPageId}/photo/{recordPhotoId}")
    suspend fun getRecordPhoto(@Path("userId")userId: Int, @Path("recordPageId")recordPageId: Int, @Path("recordPhotoId")recordPhotoId: Int): Response<RecordPhoto>
}

