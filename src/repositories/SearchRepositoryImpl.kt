package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Spot

class SearchRepositoryImpl(private val databaseHelper: DatabaseHelper): SearchRepository {

    override fun searchSpots(term: String): List<Spot> {
        return this.databaseHelper.searchSpots(term)
    }
}