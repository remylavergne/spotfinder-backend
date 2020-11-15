package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.repositories.SpotsRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.exceptions.MissingQueryParams
import dev.remylavergne.spotfinder.utils.toJson
import io.ktor.http.*

class SpotsServiceImpl(private val spotsRepository: SpotsRepository) : SpotsService {

    override fun createNewSpot(dto: SpotCreationDto): String? {
        // Create a real spot object
        val spot = Spot.fromCreationDto(dto)
        // Save spot into database
        val spotInserted = spotsRepository.persistSpot(spot)

        return if (spotInserted != null) {
            MoshiHelper.toJson(spotInserted)
        } else {
            null
        }
    }

    override fun getSpots(): String {
        return spotsRepository.getSpots().toJson()
    }

    override fun getSpotById(id: String): String? {
        val spot = spotsRepository.getSpotById(id)
        return MoshiHelper.toJson(spot)
    }

    override fun getSpotsByRider(id: String): String {
        return spotsRepository.getSpotsByRider(id).toJson()
    }

    override fun getSpotsByCountry(country: String): String {
        TODO("Not yet implemented")
    }

    override fun getLatestPaginatedSpots(queryParams: Parameters): String {
        val page = queryParams["page"]?.toInt()
        val limit = queryParams["limit"]?.toInt()
        if (page == null || limit == null) {
            throw MissingQueryParams("QueryParams 'page' and 'limit' are mandatory")
        }
        val spots = spotsRepository.getLatestPaginatedSpots(page, limit)

        val totalSpots = getSpotsCount()

        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = spots,
            pagination = Pagination(currentPage = page, itemsPerPages = spots.count(), totalItems = totalSpots)
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun getSpotsCount(): Long {
        return spotsRepository.getSpotsCount()
    }
}
