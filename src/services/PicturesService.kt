package dev.remylavergne.spotfinder.services

import io.ktor.http.content.MultiPartData

interface PicturesService {
    suspend fun savePicture(data: MultiPartData): String // TODO: Result pattern
}