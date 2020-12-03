package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewAccountDto(
    val id: String,
    val username: String,
    val password: String,
    var token: String
)

