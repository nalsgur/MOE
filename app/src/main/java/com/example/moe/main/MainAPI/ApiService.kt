package com.example.moe.MainAPI

import retrofit2.Call
import retrofit2.http.GET
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
    @GET("exhibitions/top-liked")
    fun getFilteredTopLikedEx(
        @Query("regions") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<ExFilterTopLike>>

    @GET("exhibitions/latest")
    fun getFilteredLatestEx(
        @Query("regions") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<ExFilterLatest>>

    @GET("popupStores/top-liked")
    fun getFilteredTopLikedPopup(
        @Query("regions") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<PopupFilterTopLike>>

    @GET("popupStores/latest")
    fun getFilteredLatestPopup(
        @Query("regions") region: String,
        @Query("district") district: String
    ): Call<FilterResponse<PopupFilterLatest>>

//    @GET("main/filter")
//    fun getFilteredTopLikedEx(
//        @Query("regions") region: String,
//        @Query("district") district: String
//    ): Call<FilterResponse<ExFilterTopLike>>
//
//    @GET("main/filter")
//    fun getFilteredLatestEx(
//        @Query("regions") region: String,
//        @Query("district") district: String
//    ): Call<FilterResponse<ExFilterTopLike>>
//
//    @GET("main/filter")
//    fun getFilteredTopLikedPopup(
//        @Query("regions") region: String,
//        @Query("district") district: String
//    ): Call<FilterResponse<PopupFilterLatest>>
//
//    @GET("main/filter")
//    fun getFilteredLatestPopup(
//        @Query("regions") region: String,
//        @Query("district") district: String
//    ): Call<FilterResponse<PopupFilterLatest>>
}