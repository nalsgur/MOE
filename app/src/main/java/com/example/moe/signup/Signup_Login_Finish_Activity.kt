package com.example.moe.signup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupLoginFinishBinding

class Signup_Login_Finish_Activity : AppCompatActivity() {
    private lateinit var binding :ActivitySignupLoginFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupLoginFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupLoginFinishBackbtn.setOnClickListener { finish() }
        binding.signupLoginFinishNextbtn.setOnClickListener {
//            로그인 엑티비티 넣기
//            val intent = Intent(this, loginactivity::class.java)
//            startActivity(intent)
        }

    }
}