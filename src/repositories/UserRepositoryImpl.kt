package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.User

class UserRepositoryImpl(private val databaseHelper: DatabaseHelper) : UserRepository {
    override fun logUserConnection(userId: String) {
        databaseHelper.logUserConnection(userId)
    }

    override fun getUser(id: String?, username: String?): User? {
        return when {
            !username.isNullOrEmpty() -> databaseHelper.getUserByUsername(username)
            !id.isNullOrEmpty() -> databaseHelper.getUserById(id)
            else -> null
        }
    }

    override fun insertUser(newUser: User): Boolean {
        val userFound = databaseHelper.isUsernameExist(newUser.username)

        return if (userFound) {
            false
        } else {
            databaseHelper.createUser(newUser)
        }
    }
}