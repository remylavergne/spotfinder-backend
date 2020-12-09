package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.http.Parameters

interface UserService {
    fun logUserConnection(callParams: Parameters)
    fun getUser(id: String?, username: String?): String?
    fun retrieveAccount(credentials: RetrieveAccountDto): String?
    fun createUser(username: String): String?
    fun getPictures(query: SearchWithPaginationDto): String
    fun getComments(query: SearchWithPaginationDto): String
    fun getSpots(query: SearchWithPaginationDto): String
}