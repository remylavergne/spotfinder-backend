package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetPasswordDto(
    val password: String,
    val urlToken: String,
)
