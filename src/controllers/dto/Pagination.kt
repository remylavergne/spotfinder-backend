package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(val previousPage: Int = 0, val currentPage: Int, val itemsPerPages: Int, val totalItems: Long)