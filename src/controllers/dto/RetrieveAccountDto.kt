package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RetrieveAccountDto(
    val username: String,
    val password: String
)
