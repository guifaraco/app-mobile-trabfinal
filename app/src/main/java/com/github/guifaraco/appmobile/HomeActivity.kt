package com.github.guifaraco.appmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.guifaraco.appmobile.databinding.ActivityHomeBinding

class HomeActivity: AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}