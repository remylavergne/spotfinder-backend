package dev.remylavergne.spotfinder.data

import com.mongodb.client.model.Filters.near
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import dev.remylavergne.spotfinder.data.models.*
import io.ktor.util.*
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.*


@KtorExperimentalAPI
class DatabaseHelperImpl : DatabaseHelper {
    override fun persistPicture(picture: Picture) {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        collection.insertOne(picture)
    }

    override fun persistSpot(spot: Spot): Spot? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.insertOne(spot)
            spot
        } catch (e: Exception) {
            null
        }
    }

    override fun getPicturesBySpotId(id: String): List<Picture> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.aggregate<Picture>(match(Picture::spotId eq id, Picture::allowed eq true)).toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getPictureById(id: String): Picture? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return collection.findOne(Picture::id eq id)
    }

    override fun getPicturesByRiderId(id: String): List<Picture> {
        return listOf()
    }

    override fun getSpots(): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.aggregate<Spot>(match(Spot::allowed eq true)).toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getSpotById(id: String): Spot? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.findOne(Spot::id eq id, Spot::allowed eq true)
        } catch (e: Exception) {
            null
        }
    }

    override fun getSpotsByRider(id: String): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.aggregate<Spot>(match(Spot::allowed eq true, Spot::user eq id)).toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getLatestPaginatedSpots(page: Int, limit: Int): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.find(Spot::allowed eq true)
                .descendingSort(Spot::creationDate)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getPendingSpots(id: String, page: Int, limit: Int): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.find(Spot::allowed eq false, Spot::user eq id)
                .descendingSort(Spot::creationDate)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getSpotsCount(): Long {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection
                .find(Spot::allowed eq true)
                .count()
                .toLong()
        } catch (e: Exception) {
            0
        }
    }

    override fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): List<Picture> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.find(Picture::allowed eq true, Picture::isThumbnail eq false, Picture::spotId eq id)
                .descendingSort(Picture::createdAt)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getPendingPicturesBySpotId(id: String, page: Int, limit: Int): List<Picture> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.find(Picture::allowed eq false, Picture::isThumbnail eq false, Picture::spotId eq id)
                .descendingSort(Picture::createdAt)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getPicturesCountBySpot(id: String): Long {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.find(Picture::allowed eq true, Picture::spotId eq id).count().toLong()
        } catch (e: Exception) {
            0
        }
    }

    override fun getPicturesCount(): Long {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.countDocuments()
        } catch (e: Exception) {
            0
        }
    }

    override fun isUsernameExist(username: String): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)

        return try {
            val query = text(username) // TODO: Fix this -> Wrong result happens
            val user = collection.findOne(query)
            user != null
        } catch (e: Exception) {
            false
        }
    }

    override fun getUserByUsername(username: String): User? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            val query = text(username) // TODO: Fix this -> Wrong result happens
            collection.findOne(query)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override fun getUserById(id: String): User? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            collection.findOne(User::id eq id)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override fun getUserByEmail(email: String): User? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            val emailQuery = text("\"$email\"")
            val results = collection.find(emailQuery)
                .filter { it.email?.toLowerCase().equals(email, true) }
                .toList()
            return if (results.isNotEmpty()) {
                results.first()
            } else {
                null
            }
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override fun updatePassword(userId: String, newPasswordHash: String): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            collection.findOne(User::id eq userId)?.let { user: User ->
                collection.updateOne(User::id eq user.id, setValue(User::password, newPasswordHash))
                true
            } ?: false
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    override fun saveResetPasswordToken(tokenEntity: TokenEntity): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<TokenEntity>(SpotfinderCollections.URL_TOKENS.value)
        return try {
            collection.insertOne(tokenEntity)
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    override fun createUser(user: User): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            collection.insertOne(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun updatePictureId(picture: Picture): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            val spot = collection.findOne(Spot::id eq picture.spotId)
            return when {
                spot == null -> false
                spot.pictureId == null -> {
                    collection.updateOne(Spot::id eq picture.spotId, setValue(Spot::pictureId, picture.id))
                    true
                }
                else -> {
                    true
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun getSpotWithoutAddresses(): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.find(Spot::address eq null, Spot::allowed eq true).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun updateSpotAddress(spot: Spot): Spot {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.updateOne(Spot::id eq spot.id, setValue(Spot::address, spot.address))
            spot
        } catch (e: Exception) {
            spot
        }
    }

    override fun updateSpotName(spot: Spot): Spot {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.updateOne(Spot::id eq spot.id, setValue(Spot::name, spot.name))
            spot
        } catch (e: Exception) {
            spot
        }
    }

    override fun getSpotsNearestTo(position: Position, page: Int, limit: Int): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)

        return try {
            val query: Bson = near("location", Point(position), null, null)

            collection.find(query, Spot::allowed eq true)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    override fun searchSpots(term: String): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            val query: Bson = text(term)
            collection.find(Spot::allowed eq true, query).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun addComment(data: Comment): Comment? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            collection.insertOne(data)
            data
        } catch (e: Exception) {
            null
        }
    }

    override fun getSpotComments(id: String, page: Int, limit: Int): List<Comment> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            val query: Bson = lookup(SpotfinderCollections.USERS.value, "userId", "id", "user")
            val match: Bson = match(Comment::allowed eq true, Comment::spotId eq id)
            val sort: Bson = sort(Document("createdAt", -1))
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(query, match, sort, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getPictureComments(id: String, page: Int, limit: Int): List<Comment> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            val query: Bson = lookup(SpotfinderCollections.USERS.value, "userId", "id", "user")
            val match: Bson = match(Comment::allowed eq true, Comment::pictureId eq id)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(query, match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getCommentComments(id: String, page: Int, limit: Int): List<Comment> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            val query: Bson = lookup(SpotfinderCollections.USERS.value, "userId", "id", "user")
            val match: Bson = match(Comment::allowed eq true, Comment::commentId eq id)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(query, match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUserComments(id: String, page: Int, limit: Int): List<Comment> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            val query: Bson = lookup(SpotfinderCollections.USERS.value, "userId", "id", "user")
            val match: Bson = match(Comment::allowed eq true, Comment::userId eq id)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(query, match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUserPictures(userId: String, page: Int, limit: Int): List<Picture> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            val match: Bson = match(Picture::allowed eq true, Picture::isThumbnail eq false, Picture::userId eq userId)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getPendingUserPictures(id: String, page: Int, limit: Int): List<Picture> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            val match: Bson = match(Picture::allowed eq false, Picture::isThumbnail eq false, Picture::userId eq id)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUserSpots(id: String, page: Int, limit: Int): List<Spot> {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            val match: Bson = match(Spot::allowed eq true, Spot::user eq id)
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(match, skip, limite)).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun updateUserProfilePicture(userId: String, pictureId: String): User? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            collection.findOne(User::id eq userId)?.let { user: User ->
                collection.updateOne(User::id eq userId, setValue(User::pictureId, pictureId))
                user
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun updateToken(userId: String, token: String) {
        val db = DatabaseProvider.database
        val collection = db.getCollection<TokenEntity>(SpotfinderCollections.TOKENS.value)
        try {
            val currentToken = collection.findOne(TokenEntity::userId eq userId)

            if (currentToken != null) {
                collection.updateOne(TokenEntity::userId eq userId, setValue(TokenEntity::token, token))
            } else {
                collection.insertOne(TokenEntity(userId, token))
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun getUserSpotsCount(userId: String): Int {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection
                .find(Spot::allowed eq true, Spot::user eq userId)
                .count()
        } catch (e: Exception) {
            println(e)
            0
        }
    }

    override fun getUserPicturesCount(userId: String): Int {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection
                .find(Picture::allowed eq true, Picture::isThumbnail eq false, Picture::userId eq userId)
                .count()
        } catch (e: Exception) {
            println(e)
            0
        }
    }

    override fun getUserCommentsCount(userId: String): Int {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Comment>(SpotfinderCollections.COMMENTS.value)
        return try {
            collection
                .find(Comment::allowed eq true, Comment::userId eq userId)
                .count()
        } catch (e: Exception) {
            println(e)
            0
        }
    }

    /**
     * Likes
     */

    override fun like(like: Like): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Like>(SpotfinderCollections.LIKES.value)
        return try {
            val likeFound: Like? = collection.findOne(Like::id eq like.id, Like::userId eq like.userId)

            return if (likeFound != null) {
                collection.deleteOne(Like::id eq like.id, Like::userId eq like.userId)
                true
            } else {
                collection.insertOne(like)
                true
            }
        } catch (e: Exception) {
            println(e)
            false
        }
    }
}
