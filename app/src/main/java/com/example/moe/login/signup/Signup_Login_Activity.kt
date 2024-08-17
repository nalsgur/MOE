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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupLoginBinding

class Signup_Login_Activity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupLoginBinding
    private var TAG : String = "input_idandpw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.signupLoginIdcheckbtn.isEnabled = false
        binding.signupLoginNextbtn.isEnabled = false

        binding.signupLoginBackbtn.setOnClickListener { finish() }

        binding.signupLoginIdet.addTextChangedListener(textWacher)
        binding.signupLoginPwchecket.addTextChangedListener(textWacher)
        binding.signupLoginPwet.addTextChangedListener(textWacher)

        var idcheck = true
        binding.signupLoginIdcheckbtn.setOnClickListener {
            //서버에서 중복검사
            if(idcheck) {
                binding.signupLoginIdtv.text = "사용 가능한 아이디 입니다."
                binding.signupLoginIdtv.setTextColor(getColor(R.color.navy))
            } else {
                binding.signupLoginIdtv.text = "아아디 중복입니다."
                binding.signupLoginIdtv.setTextColor(getColor(R.color.red))
            }
        }
    }

    private val textWacher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //not use
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val idtext = binding.signupLoginIdet.text.toString()
            val pwtext = binding.signupLoginPwet.text.toString()
            val pwtext_check = binding.signupLoginPwchecket.text.toString()

            if(idtext.isNotEmpty() && idtext.length==11) {
                binding.signupLoginIdcheckbtn.isEnabled = true
                binding.signupLoginIdtv.text = "중복 확인을 눌러주세요."
                binding.signupLoginIdtv.setTextColor(getColor(R.color.navy))
                binding.signupLoginIdcheckbtn.setImageResource(R.drawable.signup_login_idcheckbtn_after)
            } else {
                binding.signupLoginIdcheckbtn.isEnabled = false
                binding.signupLoginIdtv.text = "잘못된 아이디입니다."
                binding.signupLoginIdtv.setTextColor(getColor(R.color.red))
                binding.signupLoginIdcheckbtn.setImageResource(R.drawable.signup_login_idcheckbtn)
            }

            if (pwtext.length>=1) {
                if (pwtext.length >= 8) {
                    if(pwtext == pwtext_check && pwtext.isNotEmpty()){
                        binding.signupLoginPwtv.text = "비밀번호가 일치합니다."
                        binding.signupLoginPwtv.setTextColor(getColor(R.color.navy))
                    }else {
                        binding.signupLoginPwtv.text = "비밀번호가 일치하지 않습니다."
                        binding.signupLoginPwtv.setTextColor(getColor(R.color.red))
                    }
                } else {
                    binding.signupLoginPwtv.text = "비밀번호는 최소 8자리 이상이어야 합니다."
                    binding.signupLoginPwtv.setTextColor(getColor(R.color.red))
                }
            }

            binding.signupLoginNextbtn.isEnabled = idtext.isNotEmpty() && pwtext.isNotEmpty() && pwtext==pwtext_check

            //다음버튼 활성화시 엑티비티 이동
            if (binding.signupLoginNextbtn.isEnabled) {
                binding.signupLoginNextbtn.setImageResource(R.drawable.signup_nextbtn_after)
                binding.signupLoginNextbtn.setOnClickListener {
                    val intent = Intent(this@Signup_Login_Activity, Signup_Login_Name_Activity::class.java)
                    intent.putExtra("user_Id",binding.signupLoginIdet.text.toString())
                    intent.putExtra("user_Pw",binding.signupLoginPwet.text.toString())
                    intent.putExtra("user_comfirmpw",binding.signupLoginPwchecket.text.toString())
                    startActivity(intent)
                }
            } else {
                binding.signupLoginNextbtn.setImageResource(R.drawable.signup_nextbtn)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}
