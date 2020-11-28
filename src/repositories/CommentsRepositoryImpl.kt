package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Comment

class CommentsRepositoryImpl(private val databaseHelper: DatabaseHelper) : CommentsRepository {
    override fun createComment(data: CreateCommentDto): Comment? {
        val commentToAdd: Comment = Comment.fromCreationDto(data)

        return this.databaseHelper.addComment(commentToAdd)
    }

    override fun getComments(data: SearchCommentsDto, page: Int, limit: Int): List<Comment> {
        return if (data.containsOneIdAtLeast()) {
            return when {
                data.commentId != null -> this.databaseHelper.getCommentComments(data.commentId, page, limit)
                data.pictureId != null -> this.databaseHelper.getPictureComments(data.pictureId, page, limit)
                data.spotId != null -> this.databaseHelper.getSpotComments(data.spotId, page, limit)
                data.userId != null -> this.databaseHelper.getUserComments(data.userId, page, limit)
                else -> throw Exception("Error to create ")
            }
        } else {
            emptyList()
        }
    }
}