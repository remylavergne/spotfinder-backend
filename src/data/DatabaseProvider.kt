package dev.remylavergne.spotfinder.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import io.ktor.application.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.litote.kmongo.KMongo

@KtorExperimentalAPI
object DatabaseProvider {

    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase

    @Throws(Exception::class)
    fun initialize(application: Application): DatabaseProvider {
        val username = application.environment.config.property("mongodb.username")
            .getString()
        val password = application.environment.config.property(
            "mongodb.password"
        ).getString()
        val hostname = application.environment.config.property("mongodb.hostname")
            .getString()
        val port = application.environment.config.property(
            "mongodb.port"
        ).getString()
        val databaseName = application.environment.config.property("mongodb.name").getString()

        try {
            client = KMongo.createClient("mongodb://$username:$password@$hostname:$port")
            database = client.getDatabase(databaseName)
            // val collection = database.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
            // collection.insertOne(Spot(UUID.randomUUID().toString(), "TNS", "France"))
        } catch (e: Exception) {
            throw e
        }
        return this
    }
}

// Example
@Serializable
data class Spot(val id: String, val name: String, val country: String)