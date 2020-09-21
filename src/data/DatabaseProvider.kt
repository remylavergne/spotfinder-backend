package dev.remylavergne.spotfinder.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.litote.kmongo.KMongo

@KtorExperimentalAPI
object DatabaseProvider { // TODO: Refaire le database provider en injection

    lateinit var client: MongoClient
        private set
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
        } catch (e: Exception) {
            throw e
        }
        return this
    }
}
