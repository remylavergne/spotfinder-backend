package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchCommentsDto(
    val userId: String? = null,
    val spotId: String? = null,
    val pictureId: String? = null,
    val commentId: String? = null,
) {
    fun containsOneIdAtLeast(): Boolean = !(userId != null && spotId != null && pictureId != null && commentId != null)
}
