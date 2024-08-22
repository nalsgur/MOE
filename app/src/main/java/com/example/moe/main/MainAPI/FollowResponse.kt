package com.example.moe.main.MainAPI

import com.google.gson.annotations.SerializedName

data class CombinedResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String?,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("heart") var heart: Boolean = false,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

data class FollowCardItem(
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val date: String,
    var heart: Boolean = false
)

data class CardItem(val imageUrl : String, val name : String, val date : String)
