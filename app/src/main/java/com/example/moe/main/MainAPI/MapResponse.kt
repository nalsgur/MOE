package com.example.moe.MainAPI

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class FilterResponse<T>(
    @SerializedName("exhibitions") val exFilterTop: List<ExFilterTopLike>,
    @SerializedName("exhibitions") val exFilterLatest: List<ExFilterLatest>,
    @SerializedName("popupStores") val popupFilterTop: List<PopupFilterTopLike>,
    @SerializedName("popupStores") val popupFilterLatest: List<PopupFilterLatest>
)

data class ExFilterTopLike(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("place") val place: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("hert") var heart: Boolean = false,
    @SerializedName("searchDate") val searchDate: String?,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime?,
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
    @SerializedName("hert") var heart: Boolean = false,
    @SerializedName("searchDate") val searchDate: String?,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime?,
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
    @SerializedName("hert") var heart: Boolean = false,
    @SerializedName("searchDate") val searchDate: String?,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime?,
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
    @SerializedName("hert") var heart: Boolean = false,
    @SerializedName("searchDate") val searchDate: String?,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime?,
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

