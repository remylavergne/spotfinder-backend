package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCommentDto(
    val message: String,
    val userId: String,
    val spotId: String? = null,
    val pictureId: String? = null,
    val commentId: String? = null,
)
