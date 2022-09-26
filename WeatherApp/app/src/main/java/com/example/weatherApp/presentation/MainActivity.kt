package com.example.weatherApp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherApp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        App.mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
