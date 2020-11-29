package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.utils.PasswordTools
import sun.security.util.Password

class UserRepositoryImpl(private val databaseHelper: DatabaseHelper) : UserRepository {
    override fun logUserConnection(userId: String) {
        databaseHelper.logUserConnection(userId)
    }

    override fun retrieveAccount(credentials: RetrieveAccountDto): User? {
        // Get relevant user
        val user: User = this.databaseHelper.getUserByUsername(credentials.username) ?: return null
        // Check password matching
        return if (PasswordTools.isPasswordMatching(user.password!!, credentials.password)) {
            user
        } else {
            null
        }
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