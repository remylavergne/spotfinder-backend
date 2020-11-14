package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.FileHelper
import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import java.io.File

class PicturesRepositoryImpl(private val databaseHelper: DatabaseHelper) : PicturesRepository {
    override fun savePictureAsFile(parts: List<PartData>): File? {
        var spotId: String? = null
        var userId: String? = null
        var picture: File? = null

        // Separate ids from picture informations
        val idParts = parts.filterIsInstance<PartData.FormItem>()
        val pictureParts = parts.filterIsInstance<PartData.FileItem>()
        // Extract ids
        idParts.forEach { p: PartData ->
            when (p) {
                is PartData.FormItem -> {
                    when (p.name) {
                        "spotId" -> spotId = p.value
                        "userId" -> userId = p.value
                    }
                }

                else -> throw Error(" Unknow PartData")
            }
        }
        // Check values
        checkNotNull(spotId)
        checkNotNull(userId)
        // Extract picture
        pictureParts.forEach { p: PartData ->
            when (p) {
                is PartData.FileItem -> {
                    try {
                        val ext = File(p.originalFileName ?: "no_name").extension

                        val spotDirectory = FileHelper.getOrCreateUploadDir(spotId!!)

                        picture = File(
                            spotDirectory,
                            "$spotId--$userId--${System.currentTimeMillis()}.$ext"
                        )

                        p.streamProvider().use { its ->
                            picture?.outputStream()?.buffered()?.use { stream ->
                                its.copyTo(stream)
                            }
                        }
                    } catch (e: Exception) {
                        println(e) // TODO: Handle exception
                    }
                }
                else -> throw Exception("Unknown part value")
            }
        }

         return picture
    }

    override fun persistPicture(picture: File) {
        val newPicture = Picture.fromFile(picture, FileHelper.uploadDir.path)
        databaseHelper.persistPicture(newPicture)
    }

    override fun getPicturesBySpotId(id: String): List<Picture> {
        val pictures = databaseHelper.getPicturesBySpotId(id)
        return pictures
    }

    override fun getStaticPictureFile(pictureId: String): File? {
        return databaseHelper.getPictureById(pictureId)?.let {
            return File(it.path)
        }
    }
}
