package com.example.weatherApp.domain.utils

import androidx.core.content.ContextCompat.getColor
import com.example.weatherApp.R
import com.example.weatherApp.presentation.App

class ColorManager {
    fun chooseTempColor(temp: Int): Int {
        val context = App.appComponent.getContext()
        return when (temp) {
            in Int.MIN_VALUE..-15 -> getColor(context, R.color.very_cold)
            in -14..0 -> getColor(context, R.color.cold)
            in 1..10 -> getColor(context, R.color.medium)
            in 11..25 -> getColor(context, R.color.warm)
            in 26..Int.MAX_VALUE -> getColor(context, R.color.very_warm)
            else -> getColor(context, R.color.medium)
        }
    }
}
