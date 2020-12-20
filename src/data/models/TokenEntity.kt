package dev.remylavergne.spotfinder.data.models

data class TokenEntity(
    val userId: String,
    val token: String,
    val updatedAt: Long = System.currentTimeMillis()
)
