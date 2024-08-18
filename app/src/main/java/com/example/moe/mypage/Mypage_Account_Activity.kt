package com.example.moe.mypage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.WithdrawalDialogInterface
import com.example.moe.Withdrawal_Dialog
import com.example.moe.databinding.ActivityMypageAccountBinding
import com.example.moe.login.LoginActivity

class Mypage_Account_Activity : AppCompatActivity(), WithdrawalDialogInterface {
    private lateinit var binding : ActivityMypageAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMypageAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mypageAccountBackbtniv.setOnClickListener {
            finish()
        }

        binding.mypageAccountNewpwet.addTextChangedListener(textWacher)
        binding.mypageAccountNewpwChecket.addTextChangedListener(textWacher)

        binding.mypageAccountSecessionbtn.setOnClickListener {
            val customdialog = Withdrawal_Dialog(this,this)
            customdialog.show()
        }



    }

    private val textWacher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //not use
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val pwtext = binding.mypageAccountNewpwet.text.toString()
            val pwchecktext = binding.mypageAccountNewpwChecket.text.toString()

            if(pwtext == pwchecktext && pwtext.isNotEmpty()) {
                binding.mypageAccountPwtv.text = "신규 비밀번호가 일치합니다."
                binding.mypageAccountPwtv.setTextColor(getColor(R.color.navy))

            }else {
                binding.mypageAccountPwtv.text = "비밀번호가 일치하지 않습니다."
                binding.mypageAccountPwtv.setTextColor(getColor(R.color.red))
            }

            binding.mypageAccountChangebtn.isEnabled = pwtext.isNotEmpty() && pwtext.isNotEmpty() && pwtext==pwchecktext

            if (binding.mypageAccountChangebtn.isEnabled) {
                binding.mypageAccountChangebtn.setImageResource(R.drawable.mypage_account_changebtn_after)
                val intent = Intent(this@Mypage_Account_Activity, MainActivity::class.java)
                startActivity(intent)
            } else {
                binding.mypageAccountChangebtn.setImageResource(R.drawable.mypage_account_changebox)
            }

        }

        override fun afterTextChanged(s: Editable?) {
            //not use
        }

    }
    override fun onAddButtonClicked() {
        Toast.makeText(this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@Mypage_Account_Activity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onCancelButtonClicked() {
        Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show()
    }
}