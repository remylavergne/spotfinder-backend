package dev.remylavergne.spotfinder.mocks

import dev.remylavergne.spotfinder.data.DatabaseProvider
import dev.remylavergne.spotfinder.data.SpotfinderCollections
import dev.remylavergne.spotfinder.data.models.Spot
import io.ktor.application.Application
import java.util.UUID
import kotlin.random.Random
import org.koin.ktor.ext.get
import org.litote.kmongo.insertOne
import org.litote.kmongo.getCollection

object Mocks {

    fun populateDatabase(app: Application) {
        val collection = DatabaseProvider.database.getCollection<Spot>(SpotfinderCollections.SPOTS.value)
        val mock: List<Spot> = generateSpotMock()
        collection.insertOne(mock.first())
    }

    fun generateSpotMock(n: Int = 1): List<Spot> {
        val mocks = mutableListOf<Spot>()
        var i = 0
        while (i != n) {
            mocks.add(
                Spot(
                    id = UUID.randomUUID().toString(),
                    bio = bios.shuffled()[0],
                    name = cableParksName.shuffled()[0],
                    address = null,
                    country = null,
                    disciplines = 12,
                    longitude = generateLongitude(),
                    latitude = generateLatitude(),
                    creationDate = System.currentTimeMillis(),
                    modificationDate = System.currentTimeMillis(),
                    allowed = true,
                    rider = UUID.randomUUID().toString()
                )
            )
            i++
        }

        return mocks
    }

    private val bios = listOf<String>(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vitae felis vel arcu tempor viverra. Aliquam erat volutpat. Curabitur ullamcorper vitae ipsum et dictum.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vitae felis vel arcu tempor viverra. Aliquam erat volutpat. Curabitur ullamcorper vitae ipsum et dictum. Mauris leo nunc, ultricies vel viverra in, tristique id lectus. Sed mattis est eu nulla semper, ac tempor lectus volutpat. Integer vestibulum odio erat, quis iaculis enim aliquet eu. Fusce at iaculis justo, vel euismod erat. Duis at lacus ligula. Aliquam varius quam nec commodo luctus.",
        "Integer at felis tempus, malesuada dui id, euismod quam. Vivamus non tempus urna. Duis tempus ultricies diam, quis pellentesque orci tempor nec. Praesent sed nibh neque. Curabitur id molestie dolor, ut molestie lacus. Ut ullamcorper vehicula nunc ac vestibulum. Vestibulum nunc orci, elementum et ipsum nec, facilisis elementum est.",
        "Mauris enim nulla, finibus hendrerit mauris quis, cursus vestibulum lectus.",
        "Maecenas eget fermentum erat, a malesuada lectus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Curabitur bibendum vitae magna nec eleifend. Vestibulum vulputate diam non odio dictum ornare. Etiam non magna accumsan, facilisis nunc eu, luctus augue. Vestibulum interdum nisl in leo pretium, et pellentesque ligula euismod. Nam varius ex nec sagittis maximus. Aliquam dapibus quam ligula, sit amet vestibulum lacus rhoncus quis. Sed ac sem mauris. Donec tempus lorem vitae ante maximus, id vulputate massa interdum."
    )

    private val cableParksName = listOf<String>(
        "Lakeside Paradise",
        "Goodlife Cablepark",
        "Terhills Cablepark",
        "Wake-Up Cable Antwerpen",
        "The Outsider",
        "Belgium Cable Park",
        "The Spin Cablepark"
    )

    /**
     * Longitude comprise entre -180° et +180°
     */
    private fun generateLongitude(): Double {
        val r = Random.nextDouble(0.0, 180.0)
        val isNegative = Random.nextBoolean()
        return if (isNegative) {
            r * -1
        } else {
            r
        }
    }

    /**
     * Longitude comprise entre -90° et +90°
     */
    private fun generateLatitude(): Double {
        val r = Random.nextDouble(0.0, 90.0)
        val isNegative = Random.nextBoolean()
        return if (isNegative) {
            r * -1
        } else {
            r
        }
    }
}
