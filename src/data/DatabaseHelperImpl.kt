package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.util.KtorExperimentalAPI
import org.litote.kmongo.getCollection

@KtorExperimentalAPI
class DatabaseHelperImpl : DatabaseHelper {
    override fun persistPicture(picture: Picture) {
        val db = DatabaseProvider.database
        val collection = db.getCollection<Picture>(SpotfinderCollections.PICTURES.value)
        collection.insertOne(picture)
    }
}