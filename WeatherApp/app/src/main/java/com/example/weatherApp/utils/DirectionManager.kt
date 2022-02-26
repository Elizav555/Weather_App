package com.example.weatherApp.utils

import android.R.string
import kotlin.math.round
import kotlin.math.roundToInt


class DirectionManager {
    
    fun degreesToDirection(degrees: Int): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        return directions[(degrees % 360 / 45)]
    }
}