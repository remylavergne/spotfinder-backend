package dev.remylavergne.spotfinder.data

import com.mongodb.client.model.geojson.Position
import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.data.models.User

interface DatabaseHelper {
    // Spots
    fun persistSpot(spot: Spot): Spot?
    fun getSpots(): List<Spot>
    fun getSpotById(id: String): Spot?
    fun getSpotsByRider(id: String): List<Spot>
    fun getLatestPaginatedSpots(page: Int, limit: Int): List<Spot>
    fun getSpotWithoutAddresses(): List<Spot>
    fun updateSpotAddress(spot: Spot): Spot
    fun updateSpotName(spot: Spot): Spot
    fun getSpotsNearestTo(position: Position, page: Int, limit: Int): List<Spot>
    fun searchSpots(term: String): List<Spot>
    fun updatePictureId(picture: Picture): Boolean

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
    fun getUserPictures(userId: String, page: Int, limit: Int): List<Picture>

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
    fun updateUserProfilePicture(userId: String, pictureId: String): User?
    fun updateToken(userId: String, token: String)

    // Comments
    fun addComment(data: Comment): Comment?
    fun getSpotComments(id: String, page: Int, limit: Int): List<Comment>
    fun getPictureComments(id: String, page: Int, limit: Int): List<Comment>
    fun getCommentComments(id: String, page: Int, limit: Int): List<Comment>
    fun getUserComments(id: String, page: Int, limit: Int): List<Comment>
    fun getUserSpots(id: String, page: Int, limit: Int): List<Spot>

// TODO: Reorganize and document
}
