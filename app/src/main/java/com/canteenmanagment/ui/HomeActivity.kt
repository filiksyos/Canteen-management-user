package com.canteenmanagment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragment.Home.HomeFragment
import com.canteenmanagment.Fragments.MenuFragment
import com.canteenmanagment.Fragments.ProfileFragment
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)

        // Load the default fragment
        openFragment(HomeFragment())
        binding.bottomNavigationView.selectedItemId = R.id.home

        // Set up navigation listener
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> openFragment(HomeFragment())
                R.id.menu -> openFragment(MenuFragment())
                R.id.profile -> openFragment(ProfileFragment())
                else -> false
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
