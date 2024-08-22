package com.example.moe.MainAPI

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class FilterResponse<T>(
    @SerializedName("topLikedExhibitions") val topLikedExhibitions : List<ExFilterTopLike>,
    @SerializedName("latestExhibitions") val latestExhibitions : List<ExFilterLatest>,
    @SerializedName("topLikedPopupStores") val topLikedPopupStores : List<PopupFilterTopLike>,
    @SerializedName("latestPopupStores") val latestPopupStores : List<PopupFilterLatest>
)

data class ExFilterTopLike(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("heart") var heart: Boolean,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

data class ExFilterLatest(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("heart") var heart: Boolean,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

data class PopupFilterTopLike(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("heart") var heart: Boolean,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

data class PopupFilterLatest(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("heart") var heart: Boolean,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

