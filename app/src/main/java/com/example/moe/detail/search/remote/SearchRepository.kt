package com.example.moe.search.remote

import com.example.moe.detail.getRetrofit
import com.example.moe.follow.entities.Follow
import com.example.moe.detail.search.entities.Search
import retrofit2.Response

class SearchRepository() {
    private val client = getRetrofit().create(SearchRetrofitInterface::class.java)

    suspend fun getSearchData(userId: Int, keyword: String, page: Int): Response<List<Search>> = client.getSearchData(userId, keyword, page)
    suspend fun getRecentSearchData(userId: Int): Response<List<Search>> = client.getRecentSearchData(userId)
    suspend fun setLike(followId: Int): Response<Follow> = client.setLike(followId)
}