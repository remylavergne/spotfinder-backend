package dev.remylavergne.spotfinder.services

import io.ktor.application.*
import io.ktor.http.Parameters

interface UserService {
    fun logUserConnection(callParams: Parameters)
    fun getUser(id: String?, username: String?): String?
    fun createUser(username: String): Boolean
}