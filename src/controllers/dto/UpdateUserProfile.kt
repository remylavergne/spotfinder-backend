package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateUserProfile(
    val userId: String,
    val pictureId: String? = null
)
