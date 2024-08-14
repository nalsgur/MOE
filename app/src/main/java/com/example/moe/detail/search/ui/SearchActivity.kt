package com.example.moe.detail.search.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moe.R
import com.example.moe.detail.search.remote.SearchViewModel
import com.example.moe.databinding.ActivitySearchBinding
import com.example.moe.detail.PageViewModel
import com.example.moe.search.ui.SearchFragment

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val pageViewModel: PageViewModel by viewModels()
    private var curFragment = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction()
            .replace(R.id.search_frm, SearchFragment(searchViewModel))
            .commitAllowingStateLoss()

        binding.searchBackBtn.setOnClickListener {
            finish()
        }


        binding.searchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && binding.searchEt.text.length >= 2) {
                searchViewModel.getSearchData(2, binding.searchEt.text.toString(), 0)
                changeFragment(1)
                hideKeyboard()
                true
            } else {
                Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                true
            }
        }


        binding.searchEt.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && curFragment == 1) {
                changeFragment(0)
                searchViewModel.getRecentSearchData(2)
            }
        }

        searchViewModel.getRecentSearchData(2)

        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        setContentView(binding.root)
    }

    private fun Activity.hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun changeFragment(index: Int) {
        when (index) {
            0 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.search_frm, SearchFragment(searchViewModel))
                    .commitAllowingStateLoss()
                curFragment = 0
            }

            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.search_frm, SearchResultFragment(searchViewModel, pageViewModel))
                    .commitAllowingStateLoss()
                curFragment = 1
            }
        }
    }
}