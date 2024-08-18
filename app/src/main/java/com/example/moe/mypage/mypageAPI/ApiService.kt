package com.example.moe.mypage.mypageAPI

import com.example.moe.login.signup.SignupRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MypageApiService {
    @GET("user/my-page/search")
    fun mypageNickname(@Query("Keyword") keyword:String) : Call<MypageResponse>

    @POST("user/my-page/withdraw")
    fun mypagewithdraw() : Call<MypageWithDrawResponse>

    @POST("user/my-page/change-nickname?")
    fun mypageeditname(@Body request: MypageEditNameRequest) : Call<MypageEditNameResponse>
}