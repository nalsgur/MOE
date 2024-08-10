package com.example.moe.record

import com.google.gson.annotations.SerializedName

data class Memo(
    @SerializedName("body") val memo: String
)
