package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto

interface CommentsService {
    fun createComment(data: CreateCommentDto): String?
}