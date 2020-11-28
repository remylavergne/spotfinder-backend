package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.repositories.CommentsRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper

class CommentsServiceImpl(private val commentsRepository: CommentsRepository): CommentsService {

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
}