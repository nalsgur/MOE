package com.example.moe.record

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("recordPageId") val recordPageId: Long,
    @SerializedName("photo")val photo: String,
    @SerializedName("name") val name: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate")val endDate: String,
    @SerializedName("heart") val heart: Boolean
)
