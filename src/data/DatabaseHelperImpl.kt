package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import io.ktor.util.KtorExperimentalAPI
import org.litote.kmongo.*

@KtorExperimentalAPI
class DatabaseHelperImpl : DatabaseHelper {
    override fun persistPicture(picture: Picture) {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        collection.insertOne(picture)
    }

    override fun persistSpot(spot: Spot): Boolean {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        return try {
            collection.insertOne(spot)
            true
        } catch (e: Exception) {
            false
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
            collection.aggregate<Spot>(match(Spot::allowed eq true, Spot::rider eq id)).toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getSpotsByCountry(country: String): List<Spot> {
        TODO("Not yet implemented")
    }
}
