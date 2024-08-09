package com.example.moe

import retrofit2.Response

class SearchRepository() {
    private val client = RetrofitInstance.getInstance().create(RetrofitInterface::class.java)

    suspend fun getSearchData(userId: Int, keyword: String, page: Int): Response<List<Search>> = client.getSearchData(userId, keyword, page)
    suspend fun getRecentSearchData(userId: Int): Response<List<Search>> = client.getRecentSearchData(userId)
}