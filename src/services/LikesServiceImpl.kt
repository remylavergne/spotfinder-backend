package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.LikeDto
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.repositories.LikesRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import io.ktor.http.*

class LikesServiceImpl(private val likesRepository: LikesRepository) : LikesService {

    override fun updateLikeState(data: LikeDto): String {
        val success: Boolean = likesRepository.updateLikeState(data)

        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = success,
            pagination = null
        )

        return MoshiHelper.wrapperToJson(response)
    }
}