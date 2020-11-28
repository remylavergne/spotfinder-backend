package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Comment

class CommentsRepositoryImpl(private val databaseHelper: DatabaseHelper) : CommentsRepository {
    override fun createComment(data: CreateCommentDto): Comment? {
        val commentToAdd: Comment = Comment.fromCreationDto(data)

        return when {
            data.commentId != null -> this.databaseHelper.addCommentToComment(commentToAdd)
            data.pictureId != null -> this.databaseHelper.addCommentToPicture(commentToAdd)
            data.spotId != null -> this.databaseHelper.addCommentToSpot(commentToAdd)
            else -> throw Exception("Error to create ")
        }
    }
}