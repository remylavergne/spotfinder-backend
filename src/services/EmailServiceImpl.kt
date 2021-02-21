package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.models.User
import io.ktor.util.*

class EmailServiceImpl : EmailService {

    @KtorExperimentalAPI
    override fun resetPassword(user: User, url: String): Boolean {
        return false
    }
}