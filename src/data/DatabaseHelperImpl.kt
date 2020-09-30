package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import io.ktor.util.KtorExperimentalAPI
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import org.litote.kmongo.aggregate
import org.litote.kmongo.match

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
}
