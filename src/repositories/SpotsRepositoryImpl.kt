package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Spot

class SpotsRepositoryImpl(private val databaseHelper: DatabaseHelper) : SpotsRepository {
    override fun persistSpot(spot: Spot): Spot? {
        return databaseHelper.persistSpot(spot)
    }

    override fun getSpots(): List<Spot> {
        return databaseHelper.getSpots()
    }

    override fun getSpotById(id: String): Spot? {
        return databaseHelper.getSpotById(id)
    }

    override fun getSpotsByRider(id: String): List<Spot> {
       return databaseHelper.getSpotsByRider(id)
    }

    override fun getSpotsByCountry(country: String): List<Spot> {
        return databaseHelper.getSpotsByCountry(country)
    }

    override fun getPaginatedSpots(page: Int, limit: Int): List<Spot> {
        return databaseHelper.getPaginatedSpots(page, limit)
    }

    override fun getSpotsCount(): Long {
        return databaseHelper.getSpotsCount()
    }
}
