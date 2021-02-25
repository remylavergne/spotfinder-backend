package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.EmailManager
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.util.*
import org.simplejavamail.api.internal.outlooksupport.model.EmailFromOutlookMessage

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
            "<a href=\"${EmailManager.resetBaseUrl}$token\">Reset my password</a>"
        )
    }
}