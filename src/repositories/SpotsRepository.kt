package dev.remylavergne.spotfinder.repositories

import com.mongodb.client.model.geojson.Position
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot

interface SpotsRepository {
    fun persistSpot(spot: Spot): Spot?
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getLatestPaginatedSpots(page: Int, limit: Int): List<Spot>
    fun getSpotsCount(): Long
    fun updatePictureId(picture: Picture): Boolean
    fun getSpotsNearestTo(position: Position, page: Int, limit: Int): List<Spot>
}
