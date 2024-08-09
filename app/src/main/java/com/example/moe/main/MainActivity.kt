package com.example.moe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.moe.databinding.ActivityMainBinding
import com.example.moe.main.FollowFragment
import com.example.moe.main.MypageFragment
import com.example.moe.main.RecordFragment
import kotlin.math.exp

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var recordFragment: RecordFragment
    private lateinit var followFragment: FollowFragment
    private lateinit var mypageFragment: MypageFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MOE)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        mapFragment = MapFragment()
        recordFragment = RecordFragment()
        followFragment = FollowFragment()
        mypageFragment = MypageFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_frm, homeFragment, "home")
                .commitAllowingStateLoss()
        }

        initBottomvigation()
    }

    private fun initBottomvigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBtv.setOnItemSelectedListener { item ->
            when(item.itemId) {

                R.id.fragment_home -> {
                    showFragment(homeFragment, "home")
                    return@setOnItemSelectedListener true
                }

                R.id.fragment_map -> {
                    showFragment(mapFragment, "map")
                    return@setOnItemSelectedListener true
                }

                R.id.fragment_diary -> {
                    try {
                        showFragment(recordFragment, "diary")
                    } catch (e: Exception) {
                        Log.e("BottomNav", "An error occurred in RecordFragment", e)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.fragment_follow -> {
                    showFragment(followFragment, "follow")
                    return@setOnItemSelectedListener true
                }

                R.id.fragment_mypage -> {
                    showFragment(mypageFragment, "mypage")
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach { hide(it) }

            if (supportFragmentManager.findFragmentByTag(tag) != null) {
                show(fragment)
            } else {
                add(R.id.main_frm, fragment, tag)
            }
        }.commitAllowingStateLoss()
    }
}