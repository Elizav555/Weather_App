package com.example.weatherApp.domain.utils

class DirectionManager {

    fun degreesToDirection(degrees: Int): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        return directions[degrees % 360 / 45]
    }
}
