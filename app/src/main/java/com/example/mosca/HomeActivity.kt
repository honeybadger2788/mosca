package com.example.mosca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mosca.databinding.ActivityHomeBinding
import com.example.mosca.databinding.ActivityRegisterBinding

class HomeActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}