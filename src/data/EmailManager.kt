package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.User
import io.ktor.application.*
import io.ktor.util.*
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder

@KtorExperimentalAPI
object EmailManager {

    private lateinit var appName: String
    private lateinit var address: String
    private lateinit var port: String
    private lateinit var email: String
    private lateinit var password: String

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