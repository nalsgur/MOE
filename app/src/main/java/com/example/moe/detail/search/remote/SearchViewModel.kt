package com.example.moe.detail.search.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moe.follow.entities.Follow
import com.example.moe.detail.search.entities.Search
import com.example.moe.search.remote.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(): ViewModel() {
    private val repository = SearchRepository()

    private val _searchState = MutableLiveData<SearchState>()
    val searchState : LiveData<SearchState>
        get() = _searchState

    private val _recentSearchState = MutableLiveData<SearchState>()
    val recentSearchState : LiveData<SearchState>
        get() = _recentSearchState

    private val _likeState = MutableLiveData<LikeState>()
    val likeState : LiveData<LikeState>
        get() = _likeState



    init {
        _searchState.value = SearchState()
        _recentSearchState.value = SearchState()
        _likeState.value = LikeState()
    }


    fun getSearchData(userId: Int, keyword: String, page: Int){
        viewModelScope.launch {
            try {
                val response = repository.getSearchData(userId, keyword, page)

                _searchState.value = _searchState.value?.copy(
                    loading = true
                )
                if(response.isSuccessful){
                    _searchState.value = _searchState.value?.copy(
                        list = response.body(),
                        loading = false,
                        error = null
                    )
                }else{
                    Log.d("getSearchData", response.code().toString())
                }
            }catch (e: Exception){
                _searchState.value = _searchState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }

    fun getRecentSearchData(userId: Int){
        viewModelScope.launch {
            try {
                val response = repository.getRecentSearchData(userId)
                _recentSearchState.value = _recentSearchState.value?.copy(
                    loading = true
                )
                _recentSearchState.value = _recentSearchState.value?.copy(
                    list = response.body(),
                    loading = false,
                    error = null
                )
            }catch (e: Exception){
                _recentSearchState.value = _recentSearchState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }

    fun setLike(followId: Int){
        viewModelScope.launch {
            try {
                val response = repository.setLike(followId)
                _likeState.value = _likeState.value?.copy(
                    loading = true
                )
                _likeState.value = _likeState.value?.copy(
                    list = response.body(),
                    loading = false,
                    error = null
                )
            }catch (e: Exception){
                _likeState.value = _likeState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }


    data class SearchState(
        val loading: Boolean = true,
        val list: List<Search>? = arrayListOf(),
        val error: String? = null
    )

    data class LikeState(
        val loading: Boolean = true,
        val list: Follow? = null,
        val error: String? = null
    )
}

