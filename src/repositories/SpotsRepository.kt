package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Spot

interface SpotsRepository {
    fun persistSpot(spot: Spot): Boolean
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getSpotsByCountry(country: String): List<Spot>
}
