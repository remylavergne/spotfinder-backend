package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.TokenEntity
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.application.*
import io.ktor.util.*
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import kotlin.properties.Delegates

@KtorExperimentalAPI
object EmailManager {

    private lateinit var appName: String
    private lateinit var address: String
    private lateinit var port: String
    private lateinit var email: String
    private lateinit var password: String
    lateinit var resetBaseUrl: String
    private var tokenMaxValidity by Delegates.notNull<Long>()

    @Throws(Exception::class)
    fun initialize(application: Application): EmailManager {
        this.appName = application.environment.config.property("global.appname")
            .getString()
        this.address = application.environment.config.property(
            "email.smtp.address"
        ).getString()
        this.port = application.environment.config.property("email.smtp.port")
            .getString()
        this.email = application.environment.config.property(
            "email.smtp.email"
        ).getString()
        this.password = application.environment.config.property("email.smtp.password").getString()
        this.resetBaseUrl = application.environment.config.property("email.resetBaseUrl").getString()
        this.tokenMaxValidity = application.environment.config.property("email.urlTokenValidity").getString().toLong()

        return this
    }

    /**
     * Public API
     */

    fun sendEmail(user: User, subject: String, htmlText: String) {
        val email: Email = EmailBuilder.startingBlank()
            .to(user.username, user.email)
            .from(this.appName, this.email)
            .withSubject(subject)
            .withHTMLText(htmlText)
            .buildEmail()

        this.provideMailer().sendMail(email)
    }

    fun isUrlTokenStillValid(tokenEntity: TokenEntity?): Boolean {
        return tokenEntity != null && (System.currentTimeMillis() - tokenEntity.updatedAt) < this.tokenMaxValidity
    }

    /**
     * Private
     */

    private fun provideMailer(): Mailer {
        return MailerBuilder
            .withSMTPServer(this.address, this.port.toInt(), this.email, this.password)
            .withTransportStrategy(TransportStrategy.SMTP_TLS)
            .withSessionTimeout(10 * 1000)
            .withDebugLogging(true)
            .async()
            .buildMailer()
    }

}