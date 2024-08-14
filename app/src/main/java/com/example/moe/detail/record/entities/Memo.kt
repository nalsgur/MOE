package com.example.moe.detail.record.entities

import com.google.gson.annotations.SerializedName

data class Memo(
    @SerializedName("body") val memo: String
)
