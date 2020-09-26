package dev.remylavergne.spotfinder.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Picture(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Long = System.currentTimeMillis(),
    val filename: String,
    val spotId: String,
    val createdBy: String // TODO: Unique ID by phone / user
)
