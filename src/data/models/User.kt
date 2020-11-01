package dev.remylavergne.spotfinder.data.models

data class User(
    val id: String,
    val createdAt: Long,
    var lastConnexion: Long,
    val spots: List<String> = listOf(),
    val pictures: List<String> = listOf()
)