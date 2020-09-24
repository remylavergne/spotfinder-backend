package dev.remylavergne.spotfinder.data.models

import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Spot(
    var id: String,
    var bio: String,
    var name: String,
    var address: String? = null,
    var country: String? = null,
    var disciplines: Int,
    var longitude: Double,
    var latitude: Double,
    var creationDate: Long,
    var modificationDate: Long,
    var allowed: Boolean,
    var rider: String
) {
    companion object {
        fun fromCreationDto(dto: SpotCreationDto): Spot {
            return Spot(
                id = UUID.randomUUID().toString(),
                bio = dto.bio,
                name = dto.name,
                country = dto.country,
                disciplines = dto.disciplines,
                longitude = dto.longitude,
                latitude = dto.latitude,
                creationDate = System.currentTimeMillis(),
                modificationDate = System.currentTimeMillis(),
                allowed = false, // By default any data aren't public
                rider = dto.rider
            )
        }
    }
}