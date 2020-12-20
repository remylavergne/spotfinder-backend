package dev.remylavergne.spotfinder.repositories

import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.controllers.dto.UpdateUserProfile
import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
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
            !username.isNullOrEmpty() -> databaseHelper.getUserByUsername(username)?.apply { password = "" }
            !id.isNullOrEmpty() -> databaseHelper.getUserById(id)?.apply { password = "" }
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

    override fun getPictures(query: SearchWithPaginationDto): List<Picture> {
        return databaseHelper.getUserPictures(query.id, query.page, query.limit)
    }

    override fun getComments(query: SearchWithPaginationDto): List<Comment> {
        return databaseHelper.getUserComments(query.id, query.page, query.limit)
    }

    override fun getSpots(query: SearchWithPaginationDto): List<Spot> {
        return databaseHelper.getUserSpots(query.id, query.page, query.limit)
    }

    override fun updateProfile(data: UpdateUserProfile): User? {
        var user: User? = null
        data.pictureId?.let { user = databaseHelper.updateUserProfilePicture(data.userId, data.pictureId) }

        return user?.apply { password = null }
    }

    override fun updateToken(userId: String, token: String) {
        databaseHelper.updateToken(userId, token)
    }
}