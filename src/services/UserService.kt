package dev.remylavergne.spotfinder.services

import io.ktor.http.*

interface UserService {
    fun logUserConnection(callParams: Parameters)
}