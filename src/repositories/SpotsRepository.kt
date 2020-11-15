package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot

interface SpotsRepository {
    fun persistSpot(spot: Spot): Spot?
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getSpotsByCountry(country: String): List<Spot>
    fun getLatestPaginatedSpots(page: Int, limit: Int): List<Spot>
    fun getSpotsCount(): Long
    fun updatePictureId(picture: Picture): Boolean
}
