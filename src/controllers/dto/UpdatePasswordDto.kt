package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatePasswordDto(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)
