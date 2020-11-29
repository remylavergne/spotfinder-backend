package dev.remylavergne.spotfinder.data

import com.mongodb.client.model.Filters.near
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.util.*
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

    override fun getSpotsByCountry(country: String): List<Spot> {
        TODO("Not yet implemented")
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

    override fun getSpotsCount(): Long { // TODO: Prendre en compte seulement les Spots valides
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
            collection.find(Picture::allowed eq true, Picture::spotId eq id)
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

    override fun logUserConnection(userId: String) {
        /* val db = DatabaseProvider.database
         val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
         // Get -> update
         try {
             collection.findOne(User::id eq userId)?.let { user: User ->
                 collection.updateOne(User::id eq userId, setValue(User::lastConnexion, System.currentTimeMillis()))
                 return
             }
         } catch (e: Exception) {
             println(e)
         }
         // if not, create -> insert
         val currentTime = System.currentTimeMillis()
         val user = User(userId, currentTime, currentTime)
         collection.insertOne(user)
         */
    }

    override fun isUsernameExist(username: String): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)

        return try {
            val query = text(username)
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
            val query = text(username)
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
            collection.find(Spot::address eq null).toList()
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
            val query: Bson = text(term) // TODO -> Case sensitive ?

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
            val skip: Bson = skip((page - 1) * limit)
            val limite: Bson = limit(limit)

            collection.aggregate(listOf(query, match, skip, limite)).toList()
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
}
