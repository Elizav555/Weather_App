package com.example.weatherApp.utils

import android.content.Context
import androidx.core.content.ContextCompat.getColor
import com.example.weatherApp.R

class ColorManager {
    fun chooseTempColor(temp: Double, context: Context): Int {
        return when (temp) {
            in Double.MIN_VALUE..-15.0 -> getColor(context, R.color.very_cold)
            in -14.9..0.0
            -> getColor(context, R.color.cold)
            in 0.1..10.0 -> getColor(context, R.color.medium)
            in 10.1..25.0 -> getColor(context, R.color.warm)
            in 25.1..Double.MAX_VALUE -> getColor(context, R.color.very_warm)
            else -> getColor(context, R.color.medium)
        }
    }
}
