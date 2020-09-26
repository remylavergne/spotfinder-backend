package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import io.ktor.util.KtorExperimentalAPI
import org.litote.kmongo.getCollection

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
}
