package com.example.moe.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moe.databinding.ActivityMypageIntroduceBinding

class Mypage_Introduce_Activity : AppCompatActivity() {
    private lateinit var binding : ActivityMypageIntroduceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageIntroduceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mypageIntroduceBackbtn.setOnClickListener {
            finish()
        }

    }
}