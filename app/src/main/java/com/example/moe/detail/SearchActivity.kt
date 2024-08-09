package com.example.moe

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moe.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private var curFragment = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.search_frm, SearchFragment(viewModel))
            .commitAllowingStateLoss()

        binding.searchBackBtn.setOnClickListener {
            finish()
        }


        binding.searchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && binding.searchEt.text.length >= 2) {
                viewModel.getSearchData(2, binding.searchEt.text.toString(), 0)
                changeFragment(1)
                hideKeyboard()
                true
            } else {
                TODO("검색어를 입력하세요 알림창")
            }
        }


        binding.searchEt.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && curFragment == 1) {
                changeFragment(0)
                viewModel.getRecentSearchData(2)
            }
        }

        viewModel.getRecentSearchData(2)

        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

    }

    private fun Activity.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun changeFragment(index: Int){
        when(index){
            0 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.search_frm, SearchFragment(viewModel))
                    .commitAllowingStateLoss()
                curFragment = 0
            }

            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.search_frm, SearchResultFragment(viewModel))
                    .commitAllowingStateLoss()
                curFragment = 1
            }
        }
    }
}