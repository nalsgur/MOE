package com.example.moe.record

import com.google.gson.annotations.SerializedName

data class RecordPhoto(
    @SerializedName("recordPhoto") val recordPhoto: String,
    @SerializedName("recordPhotoBody") val recordPhotoBody: String
)
