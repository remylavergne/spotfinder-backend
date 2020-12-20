package dev.remylavergne.spotfinder.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexModel
import com.mongodb.client.model.Indexes
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.bson.BsonDocument
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.KMongo
import org.litote.kmongo.ensureIndex
import com.mongodb.BasicDBObject
import org.litote.kmongo.getCollection

@KtorExperimentalAPI
object DatabaseProvider {

    private lateinit var client: MongoClient
    lateinit var database: MongoDatabase
        private set

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
            this.createCollections(database)
            this.createIndexes(database)
        } catch (e: Exception) {
            throw e
        }
        return this
    }

    private fun createIndexes(database: MongoDatabase) {
        // SPOTS
        val spotCollection = database.getCollection(SpotfinderCollections.SPOTS.value)
        val textIndexes = BasicDBObject()
        textIndexes["name"] = "text"
        textIndexes["address"] = "text"
        spotCollection.ensureIndex(textIndexes)
        spotCollection.createIndex(Indexes.geo2dsphere("location"))
        // USERS
        database.getCollection(SpotfinderCollections.USERS.value)
            .createIndex(Indexes.text("username"))
    }

    private fun createCollections(database: MongoDatabase) {
        val existingCollections = database.listCollectionNames()
        // Create all collections
        SpotfinderCollections.values().forEach {
            if (!existingCollections.contains(it.value)) {
                database.createCollection(it.value)
            }
        }
    }
}
