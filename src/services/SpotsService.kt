package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import dev.remylavergne.spotfinder.data.models.Spot
import io.ktor.http.*

interface SpotsService {
    fun createNewSpot(dto: SpotCreationDto): String?
    fun getSpots(): String
    fun getSpotById(id: String): String?
    fun getSpotsByRider(id: String): String
    fun getSpotsByCountry(country: String): String
    fun getPaginatedSpots(queryParams: Parameters): String
    fun getSpotsCount(): Long
}
