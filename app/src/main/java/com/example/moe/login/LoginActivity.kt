package com.example.moe.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.moe.main.MainActivity
import com.example.moe.R
import com.example.moe.databinding.ActivityLoginBinding
import com.example.moe.login.loginManager.FacebookLoginManager
import com.example.moe.login.loginManager.KakaoLoginManager
import com.example.moe.login.loginManager.NaverLoginManager
import com.example.moe.signup.Signup_activity
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoLoginManager: KakaoLoginManager
    private lateinit var naverLoginManager: NaverLoginManager
    private lateinit var facebookLoginManager: FacebookLoginManager
    private lateinit var defaultLoginManager: DefaultLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MOE)
        installSplashScreen()

        kakaoLoginManager = KakaoLoginManager(this)
        naverLoginManager = NaverLoginManager(this)
        facebookLoginManager = FacebookLoginManager(this)

        val kakaoClientKey = resources.getString(R.string.kakao_key)
        val naverClientId = resources.getString(R.string.naver_client_id)
        val naverClientSecret = resources.getString(R.string.naver_client_secret)
        val naverClientName = resources.getString(R.string.naver_client_name)

        KakaoSdk.init(this, kakaoClientKey)
        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret, naverClientName)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.join.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.join.setOnClickListener {
            val intent = Intent(this, Signup_activity::class.java)
            startActivity(intent)
        }

        binding.findPassword.setOnClickListener {
            val intent = Intent(this, FindActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                defaultLoginManager.login(phoneNumber, password)
//                if(phoneNumber.length==11 && password.length>=8) {
//                    Toast.makeText(this, "로그인 성공하셨습니다.", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else if (phoneNumber.length!=11) {
//                    Toast.makeText(this, "아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                } else  {
//                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                }
            }
        }

        binding.loginKakao.setOnClickListener {
            kakaoLoginManager.loginWithKakao()
        }

        binding.loginNaver.setOnClickListener {
            naverLoginManager.loginWithNaver()
        }

        binding.loginFacebook.setOnClickListener {
            facebookLoginManager.loginWithFacebook()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoLoginManager.KakaoLogout()
        naverLoginManager.NaverLogout()
        facebookLoginManager.facebookLogout()
        defaultLoginManager.logout()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginManager.onActivityResult(requestCode, resultCode, data)
    }


}