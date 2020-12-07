package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto

interface CommentsService {
    fun createComment(data: CreateCommentDto): String?
    fun getComments(data: SearchCommentsDto, page: Int, limit: Int): String
}