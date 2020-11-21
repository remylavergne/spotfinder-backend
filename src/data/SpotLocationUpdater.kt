package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.GeolocationInfos
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.utils.MoshiHelper
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.client.request.get
import org.koin.java.KoinJavaComponent.inject
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
object SpotLocationUpdater {
    private val databaseHelper: DatabaseHelper by inject(DatabaseHelper::class.java)
    private val job = Job()
    private val context = CoroutineScope(job)

    fun init() {
        updateSpotAddressesMissing()

    }

    private fun updateSpotAddressesMissing() =
        context.launch {
            while (true) {
                val spots = getSpotsWithoutAddresses(databaseHelper)
                val client = HttpClient()
                // Get location informations and update Spots
                spots.forEach { spot: Spot ->
                    try {
                        val response: String =
                            client.get("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${spot.latitude}&lon=${spot.longitude}&addressdetails=0")
                        MoshiHelper.fromJson<GeolocationInfos>(response)?.let { geolocationInfos ->
                            databaseHelper.updateSpotAddress(spot.updatePosition(geolocationInfos))
                            if (nameHaveToBeUpdate(spot, geolocationInfos)) {
                                databaseHelper.updateSpotName(spot.updatePosition(geolocationInfos))
                            }
                        }
                    } catch (e: Exception) {
                        println(e)
                    }
                    delay(5.seconds.toLongMilliseconds())
                }
                client.close()
                println("----------> Background job ${System.currentTimeMillis()}")
                delay(60_000 * 60L)
            }
        }

    private fun getSpotsWithoutAddresses(databaseHelper: DatabaseHelper): List<Spot> {
        return databaseHelper.getSpotWithoutAddresses()
    }

    private fun nameHaveToBeUpdate(spot: Spot, infos: GeolocationInfos): Boolean =
        spot.name == null && infos.name != null

}