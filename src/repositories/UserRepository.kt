package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.controllers.dto.UpdateUserProfile
import dev.remylavergne.spotfinder.data.models.*

interface UserRepository {
    fun logUserConnection(userId: String)
    fun getUser(id: String?, username: String?): User?
    fun insertUser(newUser: User): Boolean
    fun retrieveAccount(credentials: RetrieveAccountDto): User?
    fun getPictures(query: SearchWithPaginationDto): List<Picture>
    fun getComments(query: SearchWithPaginationDto): List<Comment>
    fun getSpots(query: SearchWithPaginationDto): List<Spot>
    fun updateProfile(data: UpdateUserProfile): User?
    fun updateToken(userId: String, token: String)
    fun getPendingSpots(queryData: SearchWithPaginationDto): List<Spot>
    fun getPendingPictures(data: SearchWithPaginationDto): List<Picture>
    fun getUserStatistics(data: SearchWithPaginationDto): UserStatistics
}