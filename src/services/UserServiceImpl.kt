package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.Pagination
import dev.remylavergne.spotfinder.controllers.dto.ResultWrapper
import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.repositories.UserRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import java.util.*

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun logUserConnection(callParams: Parameters) {
        callParams["id"]?.let { userId: String ->
            userRepository.logUserConnection(userId)
        } ?: throw Exception("Missing user id")
    }

    override fun getUser(id: String?, username: String?): String? {
        val user = userRepository.getUser(id, username)

        return if (user != null) {
            val response = ResultWrapper(
                statusCode = HttpStatusCode.OK.value,
                result = user
            )
            MoshiHelper.wrapperToJson(response)
        } else {
            null
        }
    }

    override fun createUser(username: String): String? {
        val newUser = User.create(username)
        val result = userRepository.insertUser(newUser)
        return if (result) {
            MoshiHelper.toJson(newUser)
        } else {
            null
        }
    }
}