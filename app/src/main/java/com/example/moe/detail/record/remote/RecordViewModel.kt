package com.example.moe.detail.record.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moe.detail.record.entities.Record
import com.example.moe.detail.record.entities.RecordPage
import com.example.moe.detail.record.entities.RecordPhoto
import com.example.moe.detail.search.entities.Search
import com.example.moe.follow.entities.Follow
import kotlinx.coroutines.launch

class RecordViewModel(): ViewModel() {
    private val repository = RecordRepository()

    private val _recordState = MutableLiveData<RecordState>()
    val recordState : LiveData<RecordState>
        get() = _recordState

    private val _recordPageState = MutableLiveData<RecordPageState>()
    val recordPageState : LiveData<RecordPageState>
        get() = _recordPageState

    private val _recordPhotoState = MutableLiveData<RecordPhotoState>()
    val recordPhotoState : LiveData<RecordPhotoState>
        get() = _recordPhotoState

    init {
        _recordState.value = RecordState()
        _recordPageState.value = RecordPageState(

        )
        _recordPhotoState.value = RecordPhotoState()
    }

    fun setRecordPage(search: Search, photo : List<String>){
        _recordPageState.value = _recordPageState.value?.copy(
            loading = false,
            error = null,
            response = RecordPage(
                name = search.title,
                photo = search.photo,
                startDate = search.startDate,
                endDate = search.endDate,
                recordPhoto = photo
            )
        )
    }


    fun getRecordLatest(userId: Int, page: Int){
        viewModelScope.launch {
            try {
                _recordState.value = _recordState.value?.copy(
                    loading = true
                )
                val response = repository.getRecordLatest(userId, page)
                if(response.isSuccessful){
                    _recordState.value = _recordState.value?.copy(
                        response = response.body(),
                        loading = false,
                        error = null
                    )
                    Log.d("getRecordLatest", response.body().toString())
                }else{
                    Log.d("getRecordLatest", response.message())
                }
            }catch (e: Exception){
                _recordState.value = _recordState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
                Log.d("error", e.message.toString())
            }
        }
    }

    fun getRecordOldest(userId: Int, page: Int){
        viewModelScope.launch {
            try {
                _recordState.value = _recordState.value?.copy(
                    loading = true
                )
                val response = repository.getRecordOldest(userId, page)
                if(response.isSuccessful){
                    _recordState.value = _recordState.value?.copy(
                        response = response.body(),
                        loading = false,
                        error = null
                    )
                    Log.d("getRecordOldest", "SUCCESS")
                }else{
                    Log.d("getRecordOldest", response.message())
                }
            }catch (e: Exception){
                _recordState.value = _recordState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }

    fun getRecordPage(userId: Int, recordPageId: Int){
        viewModelScope.launch {
            try {
                _recordPageState.value = _recordPageState.value?.copy(
                    loading = true
                )
                val response = repository.getRecordPage(userId, recordPageId)

                if(response.isSuccessful){
                    _recordPageState.value = _recordPageState.value?.copy(
                        response = response.body(),
                        loading = false,
                        error = null
                    )
                    Log.d("getRecordPage", "SUCCESS")
                }else{
                    Log.d("getRecordPage", response.message())
                }

            }catch (e: Exception){
                _recordPageState.value = _recordPageState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }

    fun getRecordPhoto(userId: Int, recordPageId: Int, recordPhotoId: Int){
        viewModelScope.launch {
            try {
                _recordPhotoState.value = _recordPhotoState.value?.copy(
                    loading = true
                )
                val response = repository.getRecordPhoto(userId, recordPageId, recordPhotoId)
                if(response.isSuccessful){
                    _recordPhotoState.value = _recordPhotoState.value?.copy(
                        response = response.body(),
                        loading = false,
                        error = null
                    )
                    Log.d("getRecordPhoto", "SUCCESS")
                }else{
                    Log.d("getRecordPhoto", response.code().toString())
                }
            }catch (e: Exception){
                _recordPhotoState.value = _recordPhotoState.value?.copy(
                    loading = false,
                    error = "error fetching data ${e.message}"
                )
            }
        }
    }


    data class RecordState(
        val loading: Boolean = true,
        val response: List<Record>? = arrayListOf(),
        val error: String? = null
    )

    data class RecordPageState(
        val loading: Boolean = true,
        val response: RecordPage? = null,
        val error: String? = null
    )

    data class RecordPhotoState(
        val loading: Boolean = true,
        val response: RecordPhoto? = null,
        val error: String? = null
    )
}

