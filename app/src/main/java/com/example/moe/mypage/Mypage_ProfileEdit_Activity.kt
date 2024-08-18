package com.example.moe.mypage

import android.app.Activity
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
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.RetrofitService
import com.example.moe.changeName
import com.example.moe.databinding.ActivityMypageProfileeditBinding
import com.example.moe.mypage.mypageAPI.MypageEditNameRequest
import com.example.moe.mypage.mypageAPI.MypageEditNameResponse
import com.example.moe.mypage.mypageAPI.MypageResponse
import com.example.moe.mypage.mypageAPI.MypageRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class Mypage_ProfileEdit_Activity : AppCompatActivity() {
    private lateinit var binding : ActivityMypageProfileeditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageProfileeditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var isname = false
        binding.profileEditNameet.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //not use
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //not use
            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()) {
                    binding.profileEditNicknamebtn.setImageResource(R.drawable.profile_edit_nicknamebtn_after)
                    isname = true
                } else {
                    binding.profileEditNicknamebtn.setImageResource(R.drawable.profile_edit_nicknamebtn)
                    isname = false
                }
            }
        })

        binding.profileEditBackbtniv.setOnClickListener {
            finish()
        }

        binding.profileEditNicknamebtn.setOnClickListener {
            if(isname) {
                val request = MypageEditNameRequest(
                    binding.profileEditNameet.text.toString()
                )
                MypageRetrofitClient.apiService.mypageeditname(request).enqueue(object : Callback<MypageEditNameResponse>{
                    override fun onResponse(
                        call: Call<MypageEditNameResponse>,
                        response: Response<MypageEditNameResponse>
                    ) {
                        if(response.isSuccessful) {
                            val editnameResponse = response.body()
                            editnameResponse?.let {
                                Log.d("editname1", "message : ${it.message} ")
                            }
                        }else {
                            Log.e("editname1", "Response Failed : ${response.errorBody()?.string()}" )
                        }
                    }

                    override fun onFailure(call: Call<MypageEditNameResponse>, t: Throwable) {
                        Log.e("editname1", "실패이유 : ${t.message}" )
                    }
                })

                Toast.makeText(this, "닉네임이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.putExtra("newNickname" , binding.profileEditNameet.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }




    }
}