package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.repositories.UserRepository
import io.ktor.http.*

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun logUserConnection(callParams: Parameters) {
        callParams["id"]?.let { userId: String ->
            userRepository.logUserConnection(userId)
        } ?: throw Exception("Missing user id")
    }
}