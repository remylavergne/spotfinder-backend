package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.http.content.MultiPartData

interface PicturesService {
    suspend fun savePicture(data: MultiPartData): String // TODO: Result pattern
    suspend fun getPicturesBySpotId(id: String): String
}
