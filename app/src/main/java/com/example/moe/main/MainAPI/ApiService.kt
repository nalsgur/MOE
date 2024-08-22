package com.example.moe.MainAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // 필터링 없는 경우
    @GET("exhibitions/top-liked")
    fun getTopLikedEx(): Call<DataResponse<ExhibitionTopLiked>>

    @GET("exhibitions/latest")
    fun getLatestEx(): Call<DataResponse<ExhibitionLatest>>

    @GET("popupStores/top-liked")
    fun getTopLikedPopup(): Call<DataResponse<PopupStoresTopLiked>>

    @GET("popupStores/latest")
    fun getLatestPopup(): Call<DataResponse<PopupStoresLatest>>

    // 필터링 있는 경우
    @GET("main/filter")
    fun getFilteredTopLikedEx(
        @Query("region") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<ExFilterTopLike>>

    @GET("main/filter")
    fun getFilteredLatestEx(
        @Query("region") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<ExFilterLatest>>

    @GET("main/filter")
    fun getFilteredTopLikedPopup(
        @Query("region") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<PopupFilterTopLike>>

    @GET("main/filter")
    fun getFilteredLatestPopup(
        @Query("region") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<PopupFilterLatest>>

}