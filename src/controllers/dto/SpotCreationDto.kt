package dev.remylavergne.spotfinder.controllers.dto

data class SpotCreationDto(
    var bio: String,
    var name: String,
    var country: String,
    var disciplines: Int,
    var longitude: Double,
    var latitude: Double,
    var rider: String
)