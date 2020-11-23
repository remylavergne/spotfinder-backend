package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Position(
    val longitude: Double,
    val latitude: Double
)