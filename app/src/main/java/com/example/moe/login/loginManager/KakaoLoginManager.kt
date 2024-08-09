package com.example.moe.login.loginManager

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.moe.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLoginManager(private val context: Context) {
    private val tokenManager by lazy { TokenManger(context) }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("KAKAO ACCOUNT LOGIN", "Kakao Account Login Failed : ${error.message}")
        } else if (token != null) {
            Log.i("KAKAO ACCOUNT LOGIN", "Kakao Account Login Success : ${token.accessToken}")
            context.startActivity(Intent(context, MainActivity::class.java))
//            kakaoLoginSuccess(token)
        }
    }

    fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("KAKAO LOGIN", "Kakao Login Failed : ${error.message}")

                    //UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

                } else if (token != null) {
                    Log.i("KAKAO LOGIN", "Kakao Login Success : ${token.accessToken}")
                    context.startActivity(Intent(context, MainActivity::class.java))
//                    kakaoLoginSuccess(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun kakaoLoginSuccess(token: OAuthToken) {
        Log.d("KAKAO LOGIN", "Login Success: ${token.accessToken}")

        val service = Retrofit.instance.create(LoginApi::class.java)
        val call = service.getJwtToken("kakao", token.accessToken)

        call.enqueue(object : Callback<JwtResponse> {
            override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                if (response.isSuccessful) {
                    val jwtToken = response.body()?.jwtToken
                    jwtToken?.let {
                        tokenManager.saveJwtToken("kakao", it)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("jwt_token", jwtToken)
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                } else {
                    Log.e("KAKAO TOKEN", "Failed to get JWT token: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                Log.e("KAKAO TOKEN", "Failed to request JWT token", t)
            }
        })
    }

    fun KakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("KAKAO LOGOUT", "Logout Failed Token Deleted : ${error.message}")
            } else {
                Log.i("KAKAO LOGOUT", "Logout Success Token Deleted")
            }
        }
    }

}