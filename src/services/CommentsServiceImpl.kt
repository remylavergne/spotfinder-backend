package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.repositories.CommentsRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import io.ktor.http.*

class CommentsServiceImpl(private val commentsRepository: CommentsRepository) : CommentsService {

    override fun createComment(data: CreateCommentDto): String? {
        if (data.commentId == null && data.spotId == null && data.pictureId == null) {
            return null
        }

        val comment: Comment? = this.commentsRepository.createComment(data)

        return if (comment != null) {
            MoshiHelper.toJson(comment)
        } else {
            null
        }
    }

    override fun getComments(data: SearchCommentsDto, page: Int, limit: Int): String {
        var comments: List<Comment> = emptyList()
        if (data.containsOneIdAtLeast()) {
            comments = this.commentsRepository.getComments(data, page, limit)
        }

        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = comments,
            pagination = Pagination(
                currentPage = page,
                itemsPerPages = limit,
                totalItems = 9999
            ) // TODO: getCount
        )

        return MoshiHelper.wrapperToJson(response)
    }
}