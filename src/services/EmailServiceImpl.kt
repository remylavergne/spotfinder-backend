package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.EmailManager
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.util.*

@KtorExperimentalAPI
class EmailServiceImpl : EmailService {

    @KtorExperimentalAPI
    override fun resetPassword(user: User, url: String): Boolean {
        return false
    }

    override fun sendLinkToResetPassword(user: User, token: String) {
        EmailManager.sendEmail(
            user,
            "Password reset for your account requested!",
            "<a href=\"https://localhost:8080?token=$token\">Reset my password</a>"
        )
    }
}