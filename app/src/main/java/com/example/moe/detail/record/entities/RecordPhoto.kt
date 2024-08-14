package com.example.moe.detail.record.entities

import com.google.gson.annotations.SerializedName

data class RecordPhoto(
    @SerializedName("recordPhoto") val recordPhoto: String,
    @SerializedName("recordPhotoBody") val recordPhotoBody: String?
)
