package dev.remylavergne.spotfinder.repositories

import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import java.io.File

class PicturesRepositoryImpl : PicturesRepository {
    override fun savePictureLocally(spotIdPart: PartData, picturePart: PartData) {
        var spotId: String? = null
        when (spotIdPart.name) {
            "spotId" -> spotId = (spotIdPart as PartData.FormItem).value
            else -> throw Exception("Unknown part value")
        }
        // Backup file
        try {
            val ext = File((picturePart as PartData.FileItem).originalFileName ?: "no_name").extension

            val file = File(
                "pictures", // TODO: Get global upload dir here
                "$spotId-${picturePart.originalFileName}-${System.currentTimeMillis()}.$ext"
            )

            picturePart.streamProvider().use { its ->
                file.outputStream().buffered().use { test ->
                    its.copyTo(test)
                }
            }
        } catch (e: Exception) {
            println(e) // TODO: Handle exception
        }
    }
}