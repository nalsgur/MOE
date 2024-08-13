package com.example.moe.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.moe.R
import com.example.moe.databinding.ActivitySignupBinding
import com.example.moe.mypage.Mypage_Marketing_Activity
import com.example.moe.mypage.Mypage_Privacy_Activity
import com.example.moe.mypage.Mypage_Use_Activity

class Signup_activity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private var isAllChecked = false
    private var isUseChecked = false
    private var isPrivacyChecked = false
    private var isMarketingChecked = false
    private var isAdChecked = false

    private val viewModel : signupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupBackbtnIv.setOnClickListener { finish() }
        binding.signupPrivacylookIv.setOnClickListener {
            val intent = Intent(this, Mypage_Privacy_Activity::class.java)
            startActivity(intent)
        }
        binding.signupUsebtnlookIv.setOnClickListener {
            val intent = Intent(this, Mypage_Use_Activity::class.java)
            startActivity(intent)
        }
        binding.signupMarketinglookIv.setOnClickListener {
            val intent = Intent(this, Mypage_Marketing_Activity::class.java)
            startActivity(intent)
        }

        // All Agree Checkbox
        binding.signupCheckboxbtnIv.setOnClickListener {
            toggleAllCheckboxes()
        }
        binding.signupCheckboxbtnTv.setOnClickListener {
            toggleAllCheckboxes()
        }

        // Individual Checkboxes
        binding.signupUsebtnIv.setOnClickListener {
            toggleUseCheckbox()
        }
        binding.signupUsebtnTv.setOnClickListener {
            toggleUseCheckbox()
        }
        binding.signupPrivacybtnIv.setOnClickListener {
            togglePrivacyCheckbox()
        }
        binding.signupPrivacybtnTv.setOnClickListener {
            togglePrivacyCheckbox()
        }
        binding.signupMarketingbtnIv.setOnClickListener {
            toggleMarketingCheckbox()
        }
        binding.signupMarketingbtnTv.setOnClickListener {
            toggleMarketingCheckbox()
        }
        binding.signupAdbtnIv.setOnClickListener {
            toggleAdCheckbox()
        }
        binding.signupAdbtnTv.setOnClickListener {
            toggleAdCheckbox()
        }

        // Confirm Button
        binding.signupNextbtnIv.setOnClickListener {
            if (binding.signupNextbtnIv.isEnabled) {
                val intent = Intent(this, Signup_Login_Activity::class.java)
                startActivity(intent)
            }
        }

        // Observers to enable/disable confirm button
        viewModel.isUse.observe(this, Observer { checkConfirmButtonState() })
        viewModel.isPrivacy.observe(this, Observer { checkConfirmButtonState() })
    }

    private fun updateAllCheckedState() {
        val allChecked = isUseChecked && isPrivacyChecked && isMarketingChecked && isAdChecked
        if (isAllChecked != allChecked) {
            isAllChecked = allChecked
            binding.signupCheckboxbtnIv.setImageResource(
                if (isAllChecked) R.drawable.signup_ckeckbox_btn_after else R.drawable.signup_checkbox_btn
            )
            binding.signupCheckboxbtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))
        }
        //checkConfirmButtonState()
    }

    private fun toggleAllCheckboxes() {
        isAllChecked = !isAllChecked
        binding.signupCheckboxbtnIv.setImageResource(
            if (isAllChecked) R.drawable.signup_ckeckbox_btn_after else R.drawable.signup_checkbox_btn
        )
        binding.signupCheckboxbtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))

        isUseChecked = isAllChecked
        isPrivacyChecked = isAllChecked
        isMarketingChecked = isAllChecked
        isAdChecked = isAllChecked

        binding.signupUsebtnIv.setImageResource(if (isAllChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn)
        binding.signupUsebtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))
        binding.signupPrivacybtnIv.setImageResource(if (isAllChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn)
        binding.signupPrivacybtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))
        binding.signupMarketingbtnIv.setImageResource(if (isAllChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn)
        binding.signupMarketingbtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))
        binding.signupAdbtnIv.setImageResource(if (isAllChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn)
        binding.signupAdbtnTv.setTextColor(getColor(if (isAllChecked) R.color.black else R.color.grey_300))

        viewModel.setUse(isAllChecked)
        viewModel.setPrivacy(isAllChecked)
    }

    private fun toggleUseCheckbox() {
        isUseChecked = !isUseChecked
        binding.signupUsebtnIv.setImageResource(
            if (isUseChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn
        )
        binding.signupUsebtnTv.setTextColor(getColor(if (isUseChecked) R.color.black else R.color.grey_300))
        viewModel.toggleUse()
        updateAllCheckedState()
    }

    private fun togglePrivacyCheckbox() {
        isPrivacyChecked = !isPrivacyChecked
        binding.signupPrivacybtnIv.setImageResource(
            if (isPrivacyChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn
        )
        binding.signupPrivacybtnTv.setTextColor(getColor(if (isPrivacyChecked) R.color.black else R.color.grey_300))
        viewModel.togglePrivacy()
        updateAllCheckedState()
    }

    private fun toggleMarketingCheckbox() {
        isMarketingChecked = !isMarketingChecked
        binding.signupMarketingbtnIv.setImageResource(
            if (isMarketingChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn
        )
        binding.signupMarketingbtnTv.setTextColor(getColor(if (isMarketingChecked) R.color.black else R.color.grey_300))
        updateAllCheckedState()
    }

    private fun toggleAdCheckbox() {
        isAdChecked = !isAdChecked
        binding.signupAdbtnIv.setImageResource(
            if (isAdChecked) R.drawable.signup_check_btn_after else R.drawable.signup_check_btn
        )
        binding.signupAdbtnTv.setTextColor(getColor(if (isAdChecked) R.color.black else R.color.grey_300))
        updateAllCheckedState()
    }

    private fun checkConfirmButtonState() {
        val isEnabled = viewModel.isUse.value == true && viewModel.isPrivacy.value == true
        binding.signupNextbtnIv.isEnabled = isEnabled
        binding.signupNextbtnIv.setImageResource(
            if (isEnabled) R.drawable.signup_nextbtn_after else R.drawable.signup_nextbtn
        )
    }
}

class signupViewModel : ViewModel() {
    private val _isUse : MutableLiveData<Boolean> = MutableLiveData(false)
    val isUse : LiveData<Boolean> = _isUse
    private val _isPrivacy : MutableLiveData<Boolean> = MutableLiveData(false)
    val isPrivacy : LiveData<Boolean> = _isPrivacy

    fun toggleUse() {
        _isUse.value = !(_isUse.value ?: false)
    }

    fun togglePrivacy() {
        _isPrivacy.value = !(_isPrivacy.value ?: false)
    }

    fun setUse(value: Boolean) {
        _isUse.value = value
    }

    fun setPrivacy(value: Boolean) {
        _isPrivacy.value = value
    }
}