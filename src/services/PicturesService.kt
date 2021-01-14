package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.data.models.Picture
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.MultiPartData
import java.io.File

interface PicturesService {
    suspend fun savePicture(data: MultiPartData): String?
    suspend fun getPicturesBySpotId(id: String): String
    fun getStaticContentPicture(ac: ApplicationCall): File?
    fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): String
    fun getPicturesCountBySpotId(id: String): Long
    fun getPendingPicturesBySpotId(data: SearchWithPaginationDto): String
}
