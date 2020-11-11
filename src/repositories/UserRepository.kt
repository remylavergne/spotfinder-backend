package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.models.User

interface UserRepository {
    fun logUserConnection(userId: String)
    fun getUser(id: String?, username: String?): User?
    fun insertUser(newUser: User): Boolean
}