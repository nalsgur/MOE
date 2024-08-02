package com.example.moe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ViewModel(): ViewModel() {
    private val repository = SearchRepository()

    private val _searchState = MutableLiveData<SearchState>()
    val searchState : LiveData<SearchState>
        get() = _searchState
    private val _recentSearchState = MutableLiveData<SearchState>()
    val recentSearchState : LiveData<SearchState>
        get() = _recentSearchState
    private val _pageIndex = MutableLiveData<Int>()
    val pageIndex: LiveData<Int> get() = _pageIndex
    private val _displayIndex = MutableLiveData<Int>()
    val displayIndex: LiveData<Int> get() = _displayIndex
    private val _offset = MutableLiveData<Int>()
    val offset: LiveData<Int> get() = _offset


    init {
        _searchState.value = SearchState()
        _recentSearchState.value = SearchState()
        _pageIndex.value = 1
        _displayIndex.value = 0
        _offset.value = 0
    }

    fun increaseIndex(){
        _pageIndex.value = _pageIndex.value!! + 1
    }

    fun decreaseIndex(){
        _pageIndex.value = _pageIndex.value!! - 1
    }

    fun updateIndex(index: Int){
        _pageIndex.value = index
    }

    fun increaseDisplayIndex(){
        _displayIndex.value = _displayIndex.value!! + 1
    }

    fun decreaseDisplayIndex(){
        _displayIndex.value = _displayIndex.value!! - 1
    }

    fun increaseOffset(){
        _offset.value = _offset.value!! + 3
    }

    fun decreaseOffset(){
        _offset.value = _offset.value!! - 3
    }


    fun getSearchData(userId: Int, keyword: String, page: Int){
        viewModelScope.launch {
            try {
                _searchState.value = _searchState.value?.copy(
                    loading = true,
                )
                _searchState.value = _searchState.value?.copy(
                    list = repository.getSearchData(userId, keyword, page),
                    loading = false,
                    error = null
                )

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
                _recentSearchState.value = _recentSearchState.value?.copy(
                    loading = true,
                )
                _recentSearchState.value = _recentSearchState.value?.copy(
                    list = repository.getRecentSearchData(userId),
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

    data class SearchState(
        val loading: Boolean = true,
        val list: List<Search>? = arrayListOf(),
        val error: String? = null
    )
}

