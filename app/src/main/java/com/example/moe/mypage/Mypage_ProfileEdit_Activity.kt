package com.example.moe.mypage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.RetrofitService
import com.example.moe.changeName
import com.example.moe.databinding.ActivityMypageProfileeditBinding
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

        val changeName = HashMap<String, Any> ()
        binding.profileEditNicknamebtn.setOnClickListener {
            if(isname) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://umc.memoryofexhibition.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val retrofitService = retrofit.create(RetrofitService::class.java)
                retrofitService.changeName(changeName).enqueue(object : Callback<changeName>{
                    override fun onResponse(
                        call: Call<changeName>,
                        response: Response<changeName>
                    ) {
                        if(response.isSuccessful) {
                            val changename = response.body()
                        }
                    }

                    override fun onFailure(call: Call<changeName>, t: Throwable) {
                        Log.d("retrofit", "요청 실패")
                    }

                })




                finish()
            }
        }




    }
}