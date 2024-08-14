package com.example.moe.detail.record.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(
    @SerializedName("recordPageId") val recordPageId: Int,
    @SerializedName("photo")val photo: String,
    @SerializedName("name") val name: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate")val endDate: String,
    @SerializedName("heart") var heart: Boolean
): Parcelable
