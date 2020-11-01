package dev.remylavergne.spotfinder.utils

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object CoordinateHelper {
    fun distanceBetween(location1: Location, location2: Location): Double {
        val earthRadius = 6371.01 //Kilometers
        return earthRadius * acos(
            sin(Math.toRadians(location1.latitude)) * sin(Math.toRadians(location2.latitude)) + cos(
                Math.toRadians(
                    location1.latitude
                )
            ) * cos(
                Math.toRadians(location2.latitude)
            ) * cos(Math.toRadians(location1.longitude) - Math.toRadians(location2.longitude))
        )
    }
}

data class Location(val latitude: Double, val longitude: Double)