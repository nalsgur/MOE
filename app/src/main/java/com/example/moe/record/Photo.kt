package com.example.moe.record

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("photoUrl") val photo: List<String>,
)

