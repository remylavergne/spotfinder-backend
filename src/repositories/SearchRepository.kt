package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Spot

interface SearchRepository {
    fun searchSpots(term: String): List<Spot>
}