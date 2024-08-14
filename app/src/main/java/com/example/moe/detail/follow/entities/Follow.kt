package com.example.moe.follow.entities

import com.google.gson.annotations.SerializedName

data class Follow(
    @SerializedName("id") val id: Int,
    @SerializedName("exhibition") val exhibition: Detail,
    @SerializedName("popupStore") val popupStore: Detail,
    @SerializedName("heart") val heart: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class Detail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("searchDate") val searchDate: String
)
