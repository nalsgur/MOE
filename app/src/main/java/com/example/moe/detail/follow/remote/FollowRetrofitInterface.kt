package com.example.moe.detail.follow.remote

import com.example.moe.follow.entities.Follow
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowRetrofitInterface {

    @POST("/follows/followExhibition")
    fun followExhibition(@Query("userId")userId: Int, @Query("exhibitionId")exhibitionId: Int): Call<Follow>

    @POST("/follows/followPopupStore")
    fun followPopupStore(@Query("userId")userId: Int, @Query("popupStoreId")popupStoreId: Int): Call<Follow>

    @DELETE("/follows/unfollow/{followId}")
    fun unfollow(@Path("followId")followId: Int): Call<Void>
}