package com.example.moe.record

import com.google.gson.annotations.SerializedName

data class RecordPage(
    @SerializedName("name") val name: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("recordPhoto") val recordPhoto: List<String>
)
