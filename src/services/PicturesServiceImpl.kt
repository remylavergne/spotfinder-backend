package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.repositories.PicturesRepository
import dev.remylavergne.spotfinder.utils.toJson
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart

class PicturesServiceImpl(private val picturesRepo: PicturesRepository) : PicturesService {

    override suspend fun savePicture(data: MultiPartData): String {

        val partData = mutableListOf<PartData>()

        // Extract all parts
        data.forEachPart { part: PartData -> partData.add(part) }
        // Check if all parts needed are available
        if (partData.count() != 3) {
            return "ERROR"
        }
        // Create and Backup picture
        picturesRepo.savePictureAsFile(partData)?.let { picturesRepo.persistPicture(it) }
            ?: return "ERROR"

        return "OK"
    }

    override suspend fun getPicturesBySpotId(id: String): String {
        return picturesRepo.getPicturesBySpotId(id).toJson()
    }
}
