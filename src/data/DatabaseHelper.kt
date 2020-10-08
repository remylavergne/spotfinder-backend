package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot

interface DatabaseHelper {
    // Spots
    fun persistSpot(spot: Spot): Boolean
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getSpotsByCountry(country: String): List<Spot>

    // Pictures
    fun persistPicture(picture: Picture)
    fun getPicturesBySpotId(id: String): List<Picture>
    fun getPictureById(id: String): Picture?
    fun getPicturesByRiderId(id: String): List<Picture>
}
