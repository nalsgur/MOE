package com.example.moe.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.moe.R
import com.example.moe.databinding.ActivityNewpwBinding

class NewpwActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewpwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewpwBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.newPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.newCheckBtn.setBackgroundResource(R.drawable.login_find_check_default)
                binding.newCheckBtn.setTextColor(ContextCompat.getColor(this@NewpwActivity,
                    R.color.grey_300
                ))
                binding.newCheckBtn.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkNewPwMatch()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.newPwCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.newCheckBtn.setBackgroundResource(R.drawable.login_find_check_default)
                binding.newCheckBtn.setTextColor(ContextCompat.getColor(this@NewpwActivity,
                    R.color.grey_300
                ))
                binding.newCheckBtn.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkNewPwMatch()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.newCheckBtn.setOnClickListener {
            Toast.makeText(this@NewpwActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.newLeftArrow.setOnClickListener{
            startActivity(Intent(this, FindActivity::class.java))
        }

    }

    private fun checkNewPwMatch() {
        val newPwText = binding.newPw.text.toString()
        val pwCheckText = binding.newPwCheck.text.toString()

        if (newPwText.isNotEmpty() && newPwText.length >= 8) {
            if (newPwText == pwCheckText) {
                binding.newCheckBtn.isEnabled = true
                binding.newCheckBtn.setBackgroundResource(R.drawable.login_find_check_click)
                binding.newCheckBtn.setTextColor(Color.WHITE)
                binding.newCheckTx.text = "신규 비밀번호가 일치합니다."
                binding.newCheckTx.setTextColor(ContextCompat.getColor(this@NewpwActivity,
                    R.color.navy
                ))
            } else {
                binding.newCheckBtn.isEnabled = false
                binding.newCheckBtn.setBackgroundResource(R.drawable.login_find_check_default)
                binding.newCheckBtn.setTextColor(ContextCompat.getColor(this@NewpwActivity,
                    R.color.grey_300
                ))
                binding.newCheckTx.text = "비밀번호가 일치하지 않습니다."
                binding.newCheckTx.setTextColor(Color.RED)
            }

        } else {
            binding.newCheckBtn.isEnabled = false
            binding.newCheckBtn.setBackgroundResource(R.drawable.login_find_check_default)
            binding.newCheckBtn.setTextColor(ContextCompat.getColor(this@NewpwActivity,
                R.color.grey_300
            ))
            binding.newCheckTx.text = "비밀번호는 최소 8자리 이상이어야 합니다."
            binding.newCheckTx.setTextColor(Color.RED)
        }
    }
}