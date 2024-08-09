package com.example.moe.mypage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.databinding.ActivityMypageInputAccountBinding

class Mypage_Inputaccount_Activity : AppCompatActivity() {
    private lateinit var binding : ActivityMypageInputAccountBinding
    private var nextbtnclick = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMypageInputAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.InputAccountBackbtn.setOnClickListener {
            finish()
        }

        //비밀번호를 서버로 요청코드 작성
        var answer_pw = "000000"

        //textwatcher를 이용하여 text가 입력되는걸 실시간으로 확인
        binding.inputAccountPwet.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트가 비어있지 않은 경우
                if(!s.isNullOrEmpty()) {
                    binding.inputAccountNextbtn.setImageResource(R.drawable.input_account_nextbtn_af)
                    nextbtnclick = true
                } else {
                    binding.inputAccountNextbtn.setImageResource(R.drawable.input_account_nextbtn)
                    nextbtnclick = false
                }
            }

        })

        binding.inputAccountNextbtn.setOnClickListener {
            if(nextbtnclick) {
                var user_pw = binding.inputAccountPwet.text.toString()
                if (user_pw != answer_pw) { //비밀번호 불일치시 에러메세지 출력
                    binding.inputAccountPlease.setText("비밀번호가 일치하지 않습니다.")
                    binding.inputAccountPlease.setTextColor(getColor(R.color.red))
                } else {
                    binding.inputAccountPlease.text = "비밀번호가 일치합니다."
                    binding.inputAccountPlease.setTextColor(getColor(R.color.navy))
                    val intent = Intent(this, Mypage_Account_Activity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}