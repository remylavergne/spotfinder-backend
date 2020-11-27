package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.controllers.dto.SearchDto
import dev.remylavergne.spotfinder.repositories.SearchRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import io.ktor.http.*

class SearchServiceImpl(private val searchRepository: SearchRepository) : SearchService {
    override fun searchSpots(data: SearchDto): String {
        if (data.query.length < 3) {
            return ""
        }
        val spots = this.searchRepository.searchSpots(data.query)

        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = spots,
            pagination = Pagination(
                currentPage = 1,
                itemsPerPages = spots.count(),
                totalItems = 1000
            ) // TODO: Pagination implementation
        )

        return MoshiHelper.wrapperToJson(response)
    }
}