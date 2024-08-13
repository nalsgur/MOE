package com.example.moe.login.loginManager

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SmsSendRequest(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("confirmPhoneNumber") val confirmPhoneNumber: String
)
//data class SmsResponse(
//    @SerializedName("code") val code: String
//)

data class VertifySmsRequset(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("code") val code: String
)
//data class VertifySmsResponse( val result: String )

interface CertifyApi {
    @POST("/user/sms/send")
    fun smsSend(@Body request: SmsSendRequest): Call<ResponseBody>

    @POST("/user/sms/verify")
    fun verifySms(@Body request: VertifySmsRequset): Call<ResponseBody>
}