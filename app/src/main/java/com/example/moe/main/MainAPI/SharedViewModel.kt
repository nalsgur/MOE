package com.example.moe.MainAPI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedFilters = MutableLiveData<Map<String, List<String>>>()
    val selectedFilters: LiveData<Map<String, List<String>>> get() = _selectedFilters

    fun updateFilters(region: String, districts: List<String>) {
        val currentFilters = _selectedFilters.value?.toMutableMap() ?: mutableMapOf()
        currentFilters[region] = districts
        _selectedFilters.value = currentFilters
    }

    fun getFilters(): Map<String, List<String>> {
        return _selectedFilters.value ?: emptyMap()
    }

    fun setFilters(region: String, districts: List<String>) {
        val currentFilters = _selectedFilters.value?.toMutableMap() ?: mutableMapOf()
        currentFilters[region] = districts
        _selectedFilters.value = currentFilters
    }

}