package com.example.moe.login.loginManager

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.moe.MainActivity
import com.example.moe.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultLoginManager(private val context: Context) {
    private val loginApiSever by lazy { Retrofit.instance.create(DefaultLoginApi::class.java) }
    private val tokenManager by lazy { TokenManger(context) }

    fun login(phoneNumber: String, password: String) {
        val loginRequest = LoginRequest(phoneNumber, password)

        loginApiSever.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val jwtToken = loginResponse.jwt
                        tokenManager.saveJwtToken("default",jwtToken)
                        Toast.makeText(context, "로그인 완료되셨습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        if (context is LoginActivity) {
                            context.finish()
                        }
                    }
                } else {
                    Toast.makeText(context, "로그인 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("DEFAULT LOGIN", "Error: ${t.message}")
//                Toast.makeText(context, "로그인 실패: 서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })

    }
}