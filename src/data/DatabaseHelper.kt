package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.data.models.User

interface DatabaseHelper {
    // Spots
    fun persistSpot(spot: Spot): Boolean
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getSpotsByCountry(country: String): List<Spot>
    fun getPaginatedSpots(page: Int, limit: Int): List<Spot>

    /**
     * Get Spot allowed documents total number
     * @return value total number
     */
    fun getSpotsCount(): Long

    // Pictures
    fun persistPicture(picture: Picture)
    fun getPicturesBySpotId(id: String): List<Picture>
    fun getPictureById(id: String): Picture?
    fun getPicturesByRiderId(id: String): List<Picture>

    /**
     * Get Pictures for a specific Spot with Pagination
     */
    fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): List<Picture>

    /**
     * Get Pictures documents total number for a specific Spot
     * The result is only on allowed documents
     * @param id Spot id
     * @return total pictures for the targeted Spot
     */
    fun getPicturesCountBySpot(id: String): Long

    /**
     * Get count of all Pictures documents hosted in database
     * The result is only on allowed documents
     * @return total count
     */
    fun getPicturesCount(): Long

    // Users
    fun logUserConnection(userId: String)
    fun isUsernameExist(username: String): Boolean
    fun getUserByUsername(username: String): User?
    fun getUserById(id: String): User?
    fun createUser(user: User): Boolean
}
