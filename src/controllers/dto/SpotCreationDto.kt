package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotCreationDto(
    var name: String?,
    var longitude: Double,
    var latitude: Double,
    var user: String
)
