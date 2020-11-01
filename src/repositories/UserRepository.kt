package dev.remylavergne.spotfinder.repositories

interface UserRepository {
    fun logUserConnection(userId: String)
}