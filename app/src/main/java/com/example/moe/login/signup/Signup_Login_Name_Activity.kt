package com.example.moe.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupLoginNameBinding

class Signup_Login_Name_Activity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupLoginNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_login_name)
        binding = ActivitySignupLoginNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupLoginNameBackbtn.setOnClickListener { finish() }
        binding.signupLoginNameNextbtn.setOnClickListener {
            val intent = Intent(this, Signup_Login_Finish_Activity::class.java)
            startActivity(intent)
        }

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
                val intent = Intent(this, Signup_Login_Finish_Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}