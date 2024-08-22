package com.example.moe.MainAPI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _selectedFilters = MutableLiveData<Map<String, List<String>>>()
    val selectedFilters: LiveData<Map<String, List<String>>> get() = _selectedFilters

    fun updateFilters(region: String, districts: List<String>) {
        val currentFilters = _selectedFilters.value?.toMutableMap() ?: mutableMapOf()

        if (currentFilters[region] == districts) {
            return
        }

        currentFilters[region] = districts
        _selectedFilters.value = currentFilters
    }

    fun getFilters(): Map<String, List<String>> {
        return _selectedFilters.value ?: emptyMap()
    }

    fun setFilters(region: String, districts: List<String>) {
        val currentFilters = _selectedFilters.value?.toMutableMap() ?: mutableMapOf()

        if (currentFilters[region] == districts) {
            return
        }

        currentFilters[region] = districts
        _selectedFilters.value = currentFilters
    }

    // 하트 상태 관리 LiveData
    private val _heartStatusMap = MutableLiveData<MutableMap<String, Boolean>>(mutableMapOf())

    // FollowFragment에서 참조할 하트가 true인 아이템 목록
    private val _followedItems = MutableLiveData<List<String>>()
    val followedItems: LiveData<List<String>> get() = _followedItems

    // 고유 키 생성 메서드
    private fun generateUniqueKey(adapterType: String, itemId: String, itemName: String? = null): String {
        return if (itemName != null) {
            "$adapterType-$itemId-$itemName"
        } else {
            "$adapterType-$itemId"
        }
    }

    // 어댑터별 하트 상태 업데이트 메서드
    fun UpdateHeartStatus(adapterType: String, itemId: String, isHearted: Boolean) {
        val uniqueKey = generateUniqueKey(adapterType, itemId)
        val currentMap = _heartStatusMap.value ?: mutableMapOf()
        currentMap[uniqueKey] = isHearted
        _heartStatusMap.value = currentMap
        //updateFollowedItems()
    }

    // FollowFragment에서 사용할 하트 상태 업데이트 메서드
    fun followUpdateHeartStatus(adapterType: String, itemId: String, itemName: String, isHearted: Boolean) {
        val uniqueKey = generateUniqueKey(adapterType, itemId, itemName)
        val currentMap = _heartStatusMap.value ?: mutableMapOf()
        currentMap[uniqueKey] = isHearted
        _heartStatusMap.value = currentMap
        updateFollowedItems()
    }

    // 하트 상태가 true인 아이템을 업데이트하여 FollowFragment에 반영
    private fun updateFollowedItems() {
        val followed = mutableListOf<String>()

        _heartStatusMap.value?.let { map ->
            followed.addAll(map.filterValues { liked -> liked }.keys)
        }

        _followedItems.value = followed
    }

    // 어댑터별 하트 상태 조회 메서드
    fun getHeartStatus(adapterType: String, itemId: String): Boolean {
        val uniqueKey = generateUniqueKey(adapterType, itemId)
        return _heartStatusMap.value?.get(uniqueKey) ?: false
    }

    // FollowFragment에서 사용할 하트 상태 조회 메서드
    fun followGetHeartStatus(adapterType: String, itemId: String, itemName: String): Boolean {
        val uniqueKey = generateUniqueKey(adapterType, itemId, itemName)
        return _heartStatusMap.value?.get(uniqueKey) ?: false
    }

}