package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.data.models.Comment

interface CommentsRepository {
    fun createComment(data: CreateCommentDto): Comment?
    fun getComments(data: SearchCommentsDto, page: Int, limit: Int): List<Comment>
}