package com.example.moe.login.loginManager

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.moe.main.MainActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NaverLoginManager(private val context: Context) {
    private val tokenManager by lazy { TokenManger(context) }

    fun loginWithNaver() {
        val oathLogoinCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                var naverToken: String = ""
                naverToken = NaverIdLoginSDK.getAccessToken().toString()
                Log.i("NAVER LOGIN", "Naver Login Success : ${naverToken}")
                context.startActivity(Intent(context, MainActivity::class.java))
//                naverLoginSuccess(naverToken)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("NAVER LOGIN", "Naver Login Failed : ${errorCode}, ${errorDescription}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(context, oathLogoinCallback)
    }


    fun naverLoginSuccess(token: String) {
        Log.i("NAVER LOGIN", "Naver Login Success : ${token}")

        val service = Retrofit.instance.create(LoginApi::class.java)
        val call = service.getJwtToken("naver", token)

        call.enqueue(object : Callback<JwtResponse> {
            override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                if (response.isSuccessful) {
                    val jwtToken = response.body()?.jwtToken
                    jwtToken?.let {
                        tokenManager.saveJwtToken("naver", it)
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                } else {
                    Log.e("NAVER TOKEN", "Failed to get JWT token: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                Log.e("NAVER TOKEN", "Failed to request JWT token", t)
            }
        })
    }

    fun NaverLogout() {
        NaverIdLoginSDK.logout()
    }
}