package com.example.moe.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupLoginNameBinding
import com.example.moe.login.signup.SignupRequest
import com.example.moe.login.signup.SignupResponse
import com.example.moe.login.signup.SignupRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup_Login_Name_Activity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupLoginNameBinding
    private var TAG :String = "intent_check"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_login_name)
        binding = ActivitySignupLoginNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user_id = intent.getStringExtra("user_Id")
        val user_pw = intent.getStringExtra("user_Pw")
        val user_pwcheck = intent.getStringExtra("user_comfirmpw")

        binding.signupLoginNameBackbtn.setOnClickListener { finish() }

        var nextbtn_changed = false
        binding.signupLoginNameNameet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not use
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //not use
            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()) {
                    binding.signupLoginNameNextbtn.setImageResource(R.drawable.signup_nextbtn_after)
                    nextbtn_changed = true
                } else {
                    binding.signupLoginNameNextbtn.setImageResource(R.drawable.signup_nextbtn)
                    nextbtn_changed = false
                }
            }

        })

        binding.signupLoginNameNextbtn.setOnClickListener {
            if(nextbtn_changed) {

                // 요청 데이터 생성
                val request = SignupRequest(
                    password = user_pw.toString(),
                    confirmPassword = user_pwcheck.toString(),
                    phoneNumber = user_id.toString(),
                    nickname = binding.signupLoginNameNameet.text.toString()
                )

                SignupRetrofitClient.apiService.signup(request).enqueue(object : Callback<SignupResponse>{
                    override fun onResponse(
                        call: Call<SignupResponse>,
                        response: Response<SignupResponse>
                    ) {
                        if (response.isSuccessful) {
                            val joinResponse = response.body()
                            joinResponse?.let {
                                Log.d("signup1", "User ID: ${it.user_id}")
                                Log.d("signup1", "Created At: ${it.created_at}")
                                Log.d("signup1", "Message: ${it.message}")
                                Log.d("signup1", "Success: ${it.success}")
                            }
                        } else {
                            Log.e("signup1", "Response Failed: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                        Log.e("signup1", "실패이유 : ${t.message}")
                    }

                })
                val intent = Intent(this, Signup_Login_Finish_Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}