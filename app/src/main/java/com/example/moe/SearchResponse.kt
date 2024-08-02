package com.example.moe

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Search(
    @SerializedName("name") val title: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("followed") var follow: Boolean
): Parcelable
