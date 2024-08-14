package com.example.moe.MainAPI

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class FilterResponse<T>(
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("pageable") val pageable: Any,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<T>,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Any,
    @SerializedName("numberOfElements") val numberOfElements : Int,
    @SerializedName("first") val first : Boolean,
    @SerializedName("last") val last : Boolean,
    @SerializedName("empty") val empty : Boolean
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
    @SerializedName("regions") val region: String,
    @SerializedName("district") val district: String
)

