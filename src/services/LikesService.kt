package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.LikeDto

interface LikesService {
    fun updateLikeState(data: LikeDto): String
}