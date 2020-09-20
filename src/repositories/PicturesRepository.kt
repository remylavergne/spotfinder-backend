package dev.remylavergne.spotfinder.repositories

import io.ktor.http.content.PartData

interface PicturesRepository {
    fun savePictureLocally(spotIdPart: PartData, picturePart: PartData)
}