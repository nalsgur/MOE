package com.example.moe.detail.record.entities

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("photoUrl") val photo: List<String>,
)

