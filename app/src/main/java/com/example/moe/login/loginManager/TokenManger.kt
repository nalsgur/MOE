package com.example.moe.login.loginManager

import android.content.Context
import android.content.SharedPreferences

class TokenManger(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun saveJwtToken(provider: String, token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("${provider}_jwtToken", token)
        editor.apply()
    }

    fun getJwtToken(provider: String): String? {
        return sharedPreferences.getString("${provider}_jwtToken", null)
    }

    fun clearJwtToken(provider: String) {
        val editor = sharedPreferences.edit()
        editor.remove("${provider}_jwtToken")
        editor.apply()
    }
}