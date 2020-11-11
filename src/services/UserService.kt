package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.models.User
import io.ktor.http.Parameters

interface UserService {
    fun logUserConnection(callParams: Parameters)
    fun getUser(id: String?, username: String?): String?
    fun createUser(username: String): String?
}