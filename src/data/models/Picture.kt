package dev.remylavergne.spotfinder.data.models

import kotlinx.serialization.Serializable
import java.io.File
import java.util.UUID

@Serializable
data class Picture(
    val id: String,
    val createdAt: Long,
    val filename: String,
    val path: String,
    val spotId: String,
    val userId: String,
    val allowed: Boolean
) {
    companion object {
        fun fromFile(f: File, uploadDirBase: String): Picture {
            val infos = extractInfos(f.nameWithoutExtension)

            return Picture(
                id = UUID.randomUUID().toString(),
                createdAt = System.currentTimeMillis(),
                filename = f.name,
                path = "$uploadDirBase/${infos.spotId}/${f.name}",
                spotId = infos.spotId,
                userId = infos.userId,
                allowed = false
            )
        }

        /**
         * All mandatories informations are concat into the filename
         */
        private fun extractInfos(filename: String): Infos {
            val infos = filename.split("--")
            return Infos(spotId = infos[0], userId = infos[1])
        }
    }

    data class Infos(
        val spotId: String,
        var userId: String
    )
}
