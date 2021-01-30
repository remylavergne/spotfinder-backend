package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.LikeDto

interface LikesRepository {
    fun updateLikeState(data: LikeDto): Boolean
}