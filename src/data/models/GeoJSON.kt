package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoJSON(
    val type: String = "Point",
    val coordinates: List<Double>
)