package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.User

class UserRepositoryImpl(private val databaseHelper: DatabaseHelper) : UserRepository {
    override fun logUserConnection(userId: String) {
        databaseHelper.logUserConnection(userId)
    }

    override fun getUser(id: String?, username: String?): User? {
        return when {
            !id.isNullOrEmpty() -> databaseHelper.getUserById(id)
            !username.isNullOrEmpty() -> databaseHelper.getUserByUsername(username)
            else -> null
        }
    }
}