package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.repositories.PicturesRepository
import dev.remylavergne.spotfinder.repositories.SpotsRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.toJson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import java.io.File

class PicturesServiceImpl(private val picturesRepo: PicturesRepository, private val spotsRepository: SpotsRepository) :
    PicturesService {

    override suspend fun savePicture(data: MultiPartData): String {

        val partData = mutableListOf<PartData>()

        // Extract all parts
        data.forEachPart { part: PartData -> partData.add(part) }
        // Check if all parts needed are available
        if (partData.count() != 3) {
            return "ERROR"
        }
        // Create and Backup picture
        picturesRepo.savePictureAsFile(partData)?.let { pictureFile: File ->
            val picture = picturesRepo.persistPicture(pictureFile)
            this.spotsRepository.updatePictureId(picture)
        } ?: return "ERROR"

        return "OK" // todo: rework
    }

    override suspend fun getPicturesBySpotId(id: String): String {
        return picturesRepo.getPicturesBySpotId(id).toJson()
    }

    override fun getStaticContentPicture(ac: ApplicationCall): File? {
        val pictureId = ac.parameters["pictureId"]
        checkNotNull(pictureId)
        return picturesRepo.getStaticPictureFile(pictureId)
    }

    override fun getPaginatedPicturesBySpot(id: String, page: Int, limit: Int): String {
        val pictures = picturesRepo.getPaginatedPicturesBySpot(id, page, limit)
        val count = this.getPicturesCountBySpotId(id)

        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = pictures,
            pagination = Pagination(currentPage = page, itemsPerPages = pictures.count(), totalItems = count)
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun getPicturesCountBySpotId(id: String): Long {
        return picturesRepo.getPicturesCountBySpotId(id)
    }
}
