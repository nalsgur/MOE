package com.example.moe.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageViewModel(): ViewModel() {

    private val _pageIndex = MutableLiveData<Int>()
    val pageIndex: LiveData<Int> get() = _pageIndex

    private val _displayIndex = MutableLiveData<Int>()
    val displayIndex: LiveData<Int> get() = _displayIndex

    private val _offset = MutableLiveData<Int>()
    val offset: LiveData<Int> get() = _offset


    init {
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
}

