package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeDto(
    val id: String,
    val userId: String,
    val type: LikeType
)

enum class LikeType(val value: String) {
    SPOT("spot"),
    COMMENT("comment"),
    PICTURE("picture")
}
