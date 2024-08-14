package com.example.moe.detail.record.remote

import android.util.Log
import com.example.moe.detail.getRetrofit
import com.example.moe.detail.record.entities.Memo
import com.example.moe.detail.record.entities.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordService(){

    fun uploadPhoto(recordPageId: Int, photo: Photo) {
        val retrofit = getRetrofit().create(RecordRetrofitInterface::class.java)

        retrofit.uploadPhoto(recordPageId, photo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("uploadPhoto", "Success")
                }else{
                    Log.d("uploadPhoto", response.code().toString())
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("uploadPhoto", "Failed")
            }
        })

    }

    fun record(recordPageId: Int, recordPhotoId: Int, memo: Memo) {
        val retrofit = getRetrofit().create(RecordRetrofitInterface::class.java)

        retrofit.record(recordPageId, recordPhotoId, memo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("record", "Success")
                }else{
                    Log.d("record", response.code().toString())
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("record", "Failed")
            }
        })

    }
}