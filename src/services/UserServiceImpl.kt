package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.data.JWTTool
import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.repositories.UserRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.PasswordTools
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import java.util.*

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
}