package com.canteenmanagment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragments.MenuFragment
import com.canteenmanagment.Fragments.ProfileFragment
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityHomeBinding
import com.canteenmanagment.ui.UserOrder.UserOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)

        // Load the default fragment as UserOrderFragment
        openFragment(UserOrderFragment())
        binding.bottomNavigationView.selectedItemId = R.id.order

        // Set up navigation listener
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.order -> openFragment(UserOrderFragment())
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

