package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Spot

class SpotsRepositoryImpl(private val databaseHelper: DatabaseHelper) : SpotsRepository {
    override fun persistSpot(spot: Spot): Boolean {
        return databaseHelper.persistSpot(spot)
    }
}
