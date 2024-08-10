package com.example.moe.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.moe.R
import com.example.moe.databinding.ActivityFindBinding

class FindActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindBinding
    private var isCertifyBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIdMatch()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.findIdCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIdMatch()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.findCheckBtn.setOnClickListener{
            binding.findCertifyBody.visibility = View.VISIBLE
            binding.findCheckBtn.visibility = View.GONE
            setTimer()
        }

        binding.findCertifyTx.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.findCheckBtn.isEnabled = !s.isNullOrEmpty()
                if(!s.isNullOrEmpty()) {
                    binding.findCertifyBtn.setBackgroundResource(R.drawable.login_find_check_click)
                    binding.findCertifyBtn.setTextColor(Color.WHITE)
                    binding.findCertifyBtn.isEnabled = true
                } else {
                    binding.findCertifyBtn.setBackgroundResource(R.drawable.login_find_check_default)
                    binding.findCertifyBtn.setTextColor(ContextCompat.getColor(this@FindActivity,
                        R.color.grey_300
                    ))
                    binding.findCertifyBtn.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.findCertifyBtn.setOnClickListener {
            isCertifyBtnClicked = true
            val intent = Intent(this, NewpwActivity::class.java)
            startActivity(intent)
        }

        binding.findLeftArrow.setOnClickListener {
            isCertifyBtnClicked = true
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkIdMatch() {
        val idText = binding.findId.text.toString()
        val idCheckText = binding.findIdCheck.text.toString()

        if (idText.isNotEmpty() && idText == idCheckText) {
            binding.findCheckBtn.background = ResourcesCompat.getDrawable(resources,
                R.drawable.login_find_check_click, null)
            binding.findCheckBtn.setTextColor(Color.WHITE)
            binding.findCheckBtn.isEnabled = true
            binding.findCheckTx.text = "아이디가 일치합니다"
            binding.findCheckTx.setTextColor(ContextCompat.getColor(this, R.color.navy))
        } else {
            binding.findCheckBtn.background = ResourcesCompat.getDrawable(resources,
                R.drawable.login_find_check_default, null)
            binding.findCheckBtn.setTextColor(ContextCompat.getColor(this, R.color.grey_300))
            binding.findCheckBtn.isEnabled = false
            binding.findCheckTx.text = "아이디가 일치하지 않습니다"
            binding.findCheckTx.setTextColor(Color.RED)
        }
    }

    private fun setTimer() {
        val timer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.findCertifyTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.findCertifyTimer.text = "00:00"
                Toast.makeText(this@FindActivity, "인증시간이 만료 되었습니다", Toast.LENGTH_SHORT).show()
                binding.findCertifyRestart.visibility = View.VISIBLE
            }
        }
        timer.start()

        if (isCertifyBtnClicked) {
            timer.cancel()
            binding.findCertifyBody.visibility = View.GONE
            binding.findCheckBtn.visibility = View.VISIBLE
            binding.findId.text.clear()
            binding.findIdCheck.text.clear()
        }

        binding.findCertifyRestart.setOnClickListener {
            binding.findCertifyRestart.visibility = View.GONE
            binding.findCertifyTx.text.clear()
            setTimer()
        }
    }


}