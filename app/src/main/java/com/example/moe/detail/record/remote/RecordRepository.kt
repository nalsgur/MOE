package com.example.moe.detail.record.remote

import com.example.moe.detail.getRetrofit
import com.example.moe.detail.record.entities.RecordPage
import com.example.moe.detail.record.entities.Record
import com.example.moe.detail.record.entities.RecordPhoto
import retrofit2.Response

class RecordRepository() {
    private val client = getRetrofit().create(RecordRetrofitInterface::class.java)

    suspend fun getRecordLatest(userId: Int, page: Int): Response<List<Record>> = client.getRecordLatest(userId, page)
    suspend fun getRecordOldest(userId: Int, page: Int): Response<List<Record>> = client.getRecordOldest(userId, page)
    suspend fun getRecordPage(userId: Int, recordPageId: Int): Response<RecordPage> = client.getRecordPage(userId, recordPageId)
    suspend fun getRecordPhoto(userId: Int, recordPageId: Int, recordPhotoId: Int): Response<RecordPhoto> = client.getRecordPhoto(userId, recordPageId, recordPhotoId)
}