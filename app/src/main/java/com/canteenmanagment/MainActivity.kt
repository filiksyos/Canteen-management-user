package com.canteenmanagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.canteenmanagment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Define binding property for ActivityMainBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listener with view binding
        binding.btSignUp.setOnClickListener {
            val intent = Intent(this, SignUp_activity::class.java)
            startActivity(intent)
        }
    }
}

