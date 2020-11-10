package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val createdAt: Long,
    var lastConnexion: Long,
    val username: String? = null,
    val spots: List<String> = listOf(),
    val pictures: List<String> = listOf()
)