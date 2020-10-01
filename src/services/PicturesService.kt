package dev.remylavergne.spotfinder.services

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.MultiPartData
import java.io.File

interface PicturesService {
    suspend fun savePicture(data: MultiPartData): String
    suspend fun getPicturesBySpotId(id: String): String
    fun getStaticContentPicture(ac: ApplicationCall): File?
}
