package com.example.moe.detail.follow.remote

import android.util.Log
import com.example.moe.detail.getRetrofit
import com.example.moe.follow.entities.Follow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowService {

    fun followExhibition(userId: Int, exhibitionId: Int) {
        val retrofit = getRetrofit().create(FollowRetrofitInterface::class.java)

        retrofit.followExhibition(userId, exhibitionId).enqueue(object : Callback<Follow> {
            override fun onResponse(call: Call<Follow>, response: Response<Follow>) {
                if (response.isSuccessful) {
                    Log.d("followExhibition", "Success")
                }else{
                    Log.d("followExhibition", response.code().toString())
                }
            }
            override fun onFailure(call: Call<Follow>, t: Throwable) {
                Log.d("followExhibition", "Failed")
            }
        })

    }

    fun followPopupStore(userId: Int, exhibitionId: Int) {
        val retrofit = getRetrofit().create(FollowRetrofitInterface::class.java)

        retrofit.followPopupStore(userId, exhibitionId).enqueue(object : Callback<Follow> {
            override fun onResponse(call: Call<Follow>, response: Response<Follow>) {
                if (response.isSuccessful) {
                    Log.d("followPopupStore", "Success")
                }else{
                    Log.d("followPopupStore", response.code().toString())
                }
            }
            override fun onFailure(call: Call<Follow>, t: Throwable) {
                Log.d("followPopupStore", "Failed")
            }
        })

    }

    fun unfollow(followId: Int) {
        val retrofit = getRetrofit().create(FollowRetrofitInterface::class.java)

        retrofit.unfollow(followId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("unfollow", "Success")
                }else{
                    Log.d("unfollow", response.code().toString())
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("unfollow", "Failed")
            }
        })

    }
}