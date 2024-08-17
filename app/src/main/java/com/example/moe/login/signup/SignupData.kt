package com.example.moe.login.signup

data class SignupRequest (
    val password : String,
    val confirmPassword : String,
    val phoneNumber : String,
    val nickname : String,
    val Marketing : String = "ACTIVE",
    val Ad : String = "ACTIVE"
)

data class SignupResponse (
    val user_id : Int,
    val created_at : String,
    val message : String,
    val success : Boolean,
    val Marketing : String,
    val Ad : String,
)
