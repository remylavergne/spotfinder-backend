package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetPasswordRequestDto(
    val email: String
)
