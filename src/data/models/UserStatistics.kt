package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserStatistics(
    val userId: String,
    val spots: Int = 0,
    val pictures: Int = 0,
    val comments: Int = 0
)
