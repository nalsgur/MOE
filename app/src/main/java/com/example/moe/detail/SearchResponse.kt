package com.example.moe

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Search(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,
    @SerializedName("photo") val photo: String?,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("followed") var follow: Boolean
): Parcelable {
    companion object {
        fun default(): Search {
            return Search(
                id = 1,
                title = "기본 제목",
                startDate = "2024-07-25",
                endDate = "2024-07-25",
                photo = null,
                follow = false
            )
        }
    }
}
