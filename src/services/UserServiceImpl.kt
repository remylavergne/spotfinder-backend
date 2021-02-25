package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.*
import dev.remylavergne.spotfinder.data.JWTTool
import dev.remylavergne.spotfinder.data.models.*
import dev.remylavergne.spotfinder.repositories.UserRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.PasswordTools
import io.ktor.http.*
import io.ktor.util.*
import java.util.*

@KtorExperimentalAPI
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val emailService: EmailService
) : UserService {

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

    override fun resetPassword(data: ResetPasswordDto): Boolean {
        val user: User = userRepository.accountFound(data) ?: return false

        val urlToken = UUID.randomUUID().toString().replace("-", "")
        val isTokenSaved = this.userRepository.saveUrlToken(user.id, urlToken)

        if (!isTokenSaved) {
            return false
        }

        this.emailService.sendLinkToResetPassword(user, urlToken)

        return true
    }

    override fun resetPasswordCheckToken(token: ResetPasswordTokenDto): Boolean {
        return false
    }

    override fun getUser(id: String?, username: String?): String? {
        val user = userRepository.getUser(id, username)

        return if (user != null) {
            MoshiHelper.toJson(user)
        } else {
            null
        }
    }

    override fun createUser(userData: CreateAccountDto): String? {
        val randomPassword = PasswordTools.generatePassword()
        val newUser = User.create(userData, randomPassword)
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

    override fun getPendingPictures(data: SearchWithPaginationDto): String {
        val pictures: List<Picture> = userRepository.getPendingPictures(data)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = pictures,
            pagination = Pagination(
                currentPage = data.page,
                itemsPerPages = data.limit,
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

    override fun getPendingSpots(queryData: SearchWithPaginationDto): String {
        val spots: List<Spot> = userRepository.getPendingSpots(queryData)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = spots,
            pagination = Pagination(
                currentPage = queryData.page,
                itemsPerPages = queryData.limit,
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

    override fun getUserStatictics(data: SearchWithPaginationDto): String {
        val userStatistics: UserStatistics = userRepository.getUserStatistics(data)

        // Wrapper
        val response = ResultWrapper(
            statusCode = HttpStatusCode.OK.value,
            result = userStatistics,
            pagination = Pagination(
                currentPage = 0,
                itemsPerPages = 0,
                totalItems = 1
            )
        )

        return MoshiHelper.wrapperToJson(response)
    }

    override fun updatePassword(data: UpdatePasswordDto): Boolean {
        return userRepository.updatePassword(data)
    }
}