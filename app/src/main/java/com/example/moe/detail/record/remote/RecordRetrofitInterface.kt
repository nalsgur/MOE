package com.example.moe.detail.record.remote

import com.example.moe.detail.record.entities.Memo
import com.example.moe.detail.record.entities.RecordPage
import com.example.moe.detail.record.entities.Photo
import com.example.moe.detail.record.entities.Record
import com.example.moe.detail.record.entities.RecordPhoto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordRetrofitInterface {
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