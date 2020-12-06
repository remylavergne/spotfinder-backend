package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.http.content.PartData
import java.io.File

interface PicturesRepository {
    fun savePictureAsFile(parts: List<PartData>): File?
    fun persistPicture(picture: File): Picture
    fun getPicturesBySpotId(id: String): List<Picture>
    fun getStaticPictureFile(pictureId: String): File?
    fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): List<Picture>
    fun getPicturesCountBySpotId(id: String): Long
    fun createThumbnail(pictureFile: File): File
}
