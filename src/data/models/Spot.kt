package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Spot(
    var id: String,
    var bio: String? = null,
    var name: String?,
    var address: String? = null,
    var country: String? = null,
    var longitude: Double,
    var latitude: Double,
    var creationDate: Long,
    var modificationDate: Long,
    var allowed: Boolean,
    var user: String
) {
    companion object {
        fun fromCreationDto(dto: SpotCreationDto): Spot {
            return Spot(
                id = UUID.randomUUID().toString(),
                name = dto.name,
                longitude = dto.longitude,
                latitude = dto.latitude,
                creationDate = System.currentTimeMillis(),
                modificationDate = System.currentTimeMillis(),
                allowed = false, // By default any data aren't public
                user = dto.user
            )
        }
    }
}
