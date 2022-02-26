package com.example.weatherApp.utils

import com.example.weatherApp.R

class ColorManager {
    fun chooseTempColor(temp: Double): Int {
        return when (temp) {
            in Double.MIN_VALUE..-15.0 -> R.color.very_cold
            in -14.9..0.0 -> R.color.cold
            in 0.1..10.0 -> R.color.medium
            in 10.1..25.0 -> R.color.warm
            in 25.1..Double.MAX_VALUE -> R.color.very_warm
            else -> R.color.medium
        }
    }
}
