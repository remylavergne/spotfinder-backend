package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchWithPaginationDto(
    val id: String,
    val page: Int,
    val limit: Int
)

