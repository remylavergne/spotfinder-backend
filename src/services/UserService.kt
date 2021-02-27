package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.*

interface UserService {
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
    fun resetPasswordRequest(data: ResetPasswordRequestDto): Boolean
    fun resetPasswordCheckToken(token: ResetPasswordTokenDto): Boolean
    fun resetUserPassword(resetPasswordData: ResetPasswordDto): Boolean
}