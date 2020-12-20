package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.*
import dev.remylavergne.spotfinder.data.JWTTool
import dev.remylavergne.spotfinder.data.models.Comment
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.repositories.UserRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.PasswordTools
import io.ktor.http.*
import io.ktor.util.*

@KtorExperimentalAPI
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun logUserConnection(callParams: Parameters) {
        callParams["id"]?.let { userId: String ->
            userRepository.logUserConnection(userId)
        } ?: throw Exception("Missing user id")
    }

    override fun retrieveAccount(credentials: RetrieveAccountDto): String? {
        val user: User? = userRepository.retrieveAccount(credentials)

        return if (user != null) {
            val newToken: String = JWTTool.makeToken(user)
            user.token = newToken
            userRepository.updateToken(user.id, newToken)
            MoshiHelper.toJson(user)
        } else {
            null
        }
    }

    override fun getUser(id: String?, username: String?): String? {
        val user = userRepository.getUser(id, username)

        return if (user != null) {
            MoshiHelper.toJson(user)
        } else {
            null
        }
    }

    override fun createUser(username: String): String? {
        val randomPassword = PasswordTools.generatePassword()
        val newUser = User.create(username, randomPassword)
        val result = userRepository.insertUser(newUser)
        return if (result) {
            val token = JWTTool.makeToken(newUser)
            userRepository.updateToken(newUser.id, token)
            val dto = newUser.toNewAccountDto(randomPassword, token)
            MoshiHelper.toJson(dto)
        } else {
            null
        }
    }

    override fun getPictures(query: SearchWithPaginationDto): String {
        val pictures: List<Picture> = userRepository.getPictures(query)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = pictures,
            pagination = Pagination(
                currentPage = query.page,
                itemsPerPages = query.limit,
                totalItems = 9999
            ) // TODO: Get count
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun getComments(query: SearchWithPaginationDto): String {
        val comments: List<Comment> = userRepository.getComments(query)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = comments,
            pagination = Pagination(
                currentPage = query.page,
                itemsPerPages = query.limit,
                totalItems = 9999
            ) // TODO: Get count
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun getSpots(query: SearchWithPaginationDto): String {
        val spots: List<Spot> = userRepository.getSpots(query)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = spots,
            pagination = Pagination(
                currentPage = query.page,
                itemsPerPages = query.limit,
                totalItems = 9999
            ) // TODO: Get count
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun updateProfile(data: UpdateUserProfile): String? {
        val user: User? = userRepository.updateProfile(data)
        return if (user == null) {
            null
        } else {
            MoshiHelper.toJson(user)
        }
    }
}