package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.LikeDto
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Like

class LikesRepositoryImpl(private val databaseHelper: DatabaseHelper): LikesRepository {
    override fun updateLikeState(data: LikeDto): Boolean {
        val like: Like = Like.fromDto(data)
        return databaseHelper.like(like)
    }
}