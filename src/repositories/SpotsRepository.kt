package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Spot

interface SpotsRepository {
    fun persistSpot(spot: Spot): Boolean
}