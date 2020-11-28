package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.data.models.Comment

interface CommentsService {
    fun createComment(data: CreateCommentDto): String?
    fun getComments(data: SearchCommentsDto, page: Int, limit: Int): String
}