package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.*
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.http.Parameters

interface UserService {
    fun logUserConnection(callParams: Parameters)
    fun getUser(id: String?, username: String?): String?
    fun retrieveAccount(credentials: RetrieveAccountDto): String?
    fun createUser(userData: CreateAccountDto): String?
    fun getPictures(query: SearchWithPaginationDto): String
    fun getComments(query: SearchWithPaginationDto): String
    fun getSpots(query: SearchWithPaginationDto): String
    fun updateProfile(data: UpdateUserProfile): String?
    fun getPendingSpots(queryData: SearchWithPaginationDto): String
    fun getPendingPictures(data: SearchWithPaginationDto): String
    fun getUserStatictics(data: SearchWithPaginationDto): String
    fun updatePassword(data: UpdatePasswordDto): Boolean
    fun resetPassword(data: ResetPasswordDto): Boolean
}