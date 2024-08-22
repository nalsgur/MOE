package com.example.moe.mypage.mypageAPI

typealias MypageResponse = List<String>

data class MypageWithDrawResponse (
    val access : String
)

data class MypageEditNameResponse (
    val message : String
)

data class MypageEditNameRequest (
    val newname : String
)
