package com.example.moe.record

import com.example.moe.RetrofitInstance
import com.example.moe.RetrofitInterface
import retrofit2.Response

class RecordRepository() {
    private val client = RetrofitInstance.getInstance().create(RetrofitInterface::class.java)

    suspend fun getRecordLatest(userId: Int, page: Int): Response<List<Record>> = client.getRecordLatest(userId, page)
    suspend fun getRecordOldest(userId: Int, page: Int): Response<List<Record>> = client.getRecordOldest(userId, page)
    suspend fun getRecordPage(userId: Int, recordPageId: Int): Response<RecordPage> = client.getRecordPage(userId, recordPageId)
    suspend fun getRecordPhoto(userId: Int, recordPageId: Int, recordPhotoId: Int): Response<RecordPhoto> = client.getRecordPhoto(userId, recordPageId, recordPhotoId)
}