package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.repositories.PicturesRepository
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart

class PicturesServiceImpl(private val picturesRepo: PicturesRepository) : PicturesService {

    override suspend fun savePicture(data: MultiPartData): String {

        val partData = mutableListOf<PartData>()
        // Extract all parts
        data.forEachPart { part: PartData -> partData.add(part) }
        // Get SpotId
        val spotIdPart = partData.find { it is PartData.FormItem } ?: return "Error spotId missing"
        val picturePart = partData.find { it is PartData.FileItem } ?: return "Error picture missing"
        // Create and Backup picture
        picturesRepo.savePictureLocally(spotIdPart, picturePart)

        return "OK" // TODO: Change this
    }
}