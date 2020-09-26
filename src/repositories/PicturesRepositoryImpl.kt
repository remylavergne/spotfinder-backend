package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import java.io.File
import java.util.UUID

class PicturesRepositoryImpl(private val databaseHelper: DatabaseHelper) : PicturesRepository {
    override fun savePictureAsFile(spotIdPart: PartData, picturePart: PartData): File? {
        var spotId: String? = null
        when (spotIdPart.name) {
            "spotId" -> spotId = (spotIdPart as PartData.FormItem).value
            else -> throw Exception("Unknown part value")
        }
        // Backup file
        try {
            val ext = File((picturePart as PartData.FileItem).originalFileName ?: "no_name").extension

            // TODO: Create a specific folder by Spot "/pictures/<spotId>/"
            val picture = File(
                "pictures", // TODO: Get global upload dir here
                "$spotId-${picturePart.originalFileName}-${System.currentTimeMillis()}.$ext"
            )

            picturePart.streamProvider().use { its ->
                picture.outputStream().buffered().use { test ->
                    its.copyTo(test)
                }
            }

            return picture
        } catch (e: Exception) {
            println(e) // TODO: Handle exception
        }

        return null
    }

    override fun persistPicture(picture: File) {
        val filename: String = picture.nameWithoutExtension
        val infos = filename.split("-")
        val spotId = infos[0]
        // val riderId = infos[1]
        // TODO: Récupérer l'identifiant dans le path
        val newPicture: Picture =
            Picture(filename = filename, createdBy = UUID.randomUUID().toString(), spotId = spotId)

        databaseHelper.persistPicture(newPicture)
    }
}
