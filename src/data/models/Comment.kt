package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import com.sun.org.apache.xpath.internal.operations.Bool
import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import java.util.*

@JsonClass(generateAdapter = true)
data class Comment(
    val id: String,
    val createdAt: Long,
    val message: String,
    val userId: String,
    val allowed: Boolean = false,
    val spotId: String? = null,
    val pictureId: String? = null,
    val commentId: String? = null,
    val child: Boolean = false,
    val user: List<User> = emptyList()
) {
    companion object {
        fun fromCreationDto(dto: CreateCommentDto): Comment {
            return Comment(
                id = UUID.randomUUID().toString(),
                createdAt = System.currentTimeMillis(),
                message = dto.message,
                userId = dto.userId,
                spotId = dto.spotId,
                pictureId = dto.pictureId,
                commentId = dto.commentId
            )
        }
    }
}
