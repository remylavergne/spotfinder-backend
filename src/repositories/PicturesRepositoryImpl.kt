package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.FileHelper
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.utils.removeExtension
import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
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
                        println(e)
                    }
                }
                else -> throw Exception("Unknown part value")
            }
        }

        return picture
    }

    override fun createThumbnail(pictureFile: File): File {
        val extension = ".jpg"
        val pathParts = pictureFile.path.split("/").toMutableList()
        val newFileName = "thumbnail_${pathParts[2]}"
        pathParts[2] = newFileName
        val finalPath = pathParts.joinToString("/").removeExtension()
        val outputFile = File("$finalPath$extension")

        Thumbnails.of(pictureFile)
            .size(200, 200)
            .keepAspectRatio(true)
            .crop(Positions.CENTER)
            .outputFormat("jpg")
            .toFile(outputFile)

        return outputFile
    }

    override fun persistPicture(picture: File): Picture {
        val thumbnailFile: File = this.createThumbnail(picture)
        val pictures = Picture.fromFile(picture, thumbnailFile, FileHelper.uploadDir.path)
        databaseHelper.persistPicture(pictures.picture)
        databaseHelper.persistPicture(pictures.thumbnail)
        return pictures.picture
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

    override fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): List<Picture> {
        return databaseHelper.getPaginatedPicturesBySpot(id, page, limit)
    }

    override fun getPicturesCountBySpotId(id: String): Long {
        return databaseHelper.getPicturesCountBySpot(id)
    }
}
