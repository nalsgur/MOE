package com.example.moe.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupLoginFinishBinding
import com.example.moe.login.LoginActivity

class Signup_Login_Finish_Activity : AppCompatActivity() {
    private lateinit var binding :ActivitySignupLoginFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupLoginFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupLoginFinishBackbtn.setOnClickListener { finish() }
        binding.signupLoginFinishNextbtn.setOnClickListener {
            //로그인 화면 이동
            val intent = Intent(this, LoginActivity::class.java)
            //백스택 제거
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}