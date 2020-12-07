package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import java.io.File
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Picture(
    val id: String,
    val createdAt: Long,
    val filename: String,
    val path: String,
    val spotId: String,
    val userId: String,
    val allowed: Boolean = false,
    val isThumbnail: Boolean = false
) {
    companion object {
        fun fromFile(pictureFile: File, thumbnailFile: File, uploadDirBase: String): PictureWithThumbnail {
            val infos = extractInfos(pictureFile.nameWithoutExtension)
            val picture = Picture(
                id = UUID.randomUUID().toString(),
                createdAt = System.currentTimeMillis(),
                filename = pictureFile.name,
                path = "$uploadDirBase/${infos.spotId}/${pictureFile.name}",
                spotId = infos.spotId,
                userId = infos.userId,
                allowed = false
            )
            val thumbnail = Picture(
                id = "thumbnail_${picture.id}",
                createdAt = picture.createdAt,
                filename = pictureFile.name,
                path = thumbnailFile.path,
                spotId = infos.spotId,
                userId = infos.userId,
                allowed = false,
                isThumbnail = true
            )

            return PictureWithThumbnail(picture, thumbnail)
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

data class PictureWithThumbnail(
    val picture: Picture,
    val thumbnail: Picture
)
