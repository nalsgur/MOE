package com.example.moe.login.loginManager

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)

data class LoginResponse(
    val jwt: String
)