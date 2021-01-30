package dev.remylavergne.spotfinder.data.models

import dev.remylavergne.spotfinder.controllers.dto.LikeDto

data class Like(
    val id: String,
    val userId: String,
    val type: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromDto(dto: LikeDto): Like = Like(dto.id, dto.userId, dto.type.value)
    }
}
