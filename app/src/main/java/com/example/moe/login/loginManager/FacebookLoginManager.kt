package com.example.moe.login.loginManager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.moe.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Arrays

class FacebookLoginManager(private val context: Context) {
    private val callbackManager = CallbackManager.Factory.create()
    private val tokenManager by lazy { TokenManger(context) }

    fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(context as Activity, Arrays.asList("public_profile"))

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val facebookToken = result.accessToken.token
                Log.i("FACEBOOK LOGIN", "Facebook Login Success : ${facebookToken}")
                context.startActivity(Intent(context, MainActivity::class.java))
                //facebookLoginSuccess(facebookToken)
            }

            override fun onCancel() {
                Log.e("FACEBOOK LOGIN", "Facebook Login Cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.e("FACEBOOK LOGIN", "Facebook Login Faild : ${error.message}")
            }
        })

    }

    fun facebookLoginSuccess(token : String) {
        Log.i("FACEBOOK LOGIN", "Facebook Login Success : ${token}")

        val service = Retrofit.instance.create(LoginApi::class.java)
        val call = service.getJwtToken("facebook", token)

        call.enqueue(object : Callback<JwtResponse> {
            override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                if (response.isSuccessful) {
                    val jwtToken = response.body()?.jwtToken
                    jwtToken?.let {
                        tokenManager.saveJwtToken("facebook", it)
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                } else {
                    Log.e("FACEBOOK TOKEN", "Failed to get JWT token: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                Log.e("FACEBOOK TOKEN", "Failed to request JWT token", t)
            }
        })
    }

    fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    fun onActivityResult(requestCode : Int, resultCode : Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }



}