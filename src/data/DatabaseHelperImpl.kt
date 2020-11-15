package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.util.KtorExperimentalAPI
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
            collection.find(match(Picture::allowed eq true, Picture::spotId eq id))
                .descendingSort(Picture::createdAt)
                .skip((page - 1) * limit)
                .limit(limit)
                .toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    // TODO: A tester !
    override fun getPicturesCountBySpot(id: String): Long {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        return try {
            collection.find(match(Picture::allowed eq true, Picture::spotId eq id)).count().toLong()
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
        val existingUsernames = collection.distinct(User::username).filterNotNull().map { it.toLowerCase() }
        val isUsernameAlreadyExist = existingUsernames.find { it == username.toLowerCase() }

        return !isUsernameAlreadyExist.isNullOrEmpty()
    }

    override fun getUserByUsername(username: String): User? {
        val db = DatabaseProvider.database
        val collection = db.getCollection<User>(SpotfinderCollections.USERS.value)
        return try {
            val existingUsernames = collection.distinct(User::username).filterNotNull().map { it.toLowerCase() }
            val isUsernameAlreadyExist = existingUsernames.find { it == username.toLowerCase() }

            return if (isUsernameAlreadyExist.isNullOrEmpty()) {
                null
            } else {
                collection.findOne(User::username eq username)
            }
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
                spot == null ->  false
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
}
