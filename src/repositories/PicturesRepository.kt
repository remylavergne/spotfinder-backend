package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.http.content.PartData
import java.io.File

interface PicturesRepository {
    fun savePictureAsFile(parts: List<PartData>): File?
    fun persistPicture(picture: File)
    fun getPicturesBySpotId(id: String): List<Picture>
    fun getStaticPictureFile(pictureId: String): File?
}
