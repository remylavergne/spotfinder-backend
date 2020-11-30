package dev.remylavergne.spotfinder.data

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.remylavergne.spotfinder.data.models.User
import io.ktor.application.*
import io.ktor.util.*
import java.util.*

@KtorExperimentalAPI
object JWTTool {

    private const val secret = "zAP5MBA4B4Ijz0MZaS48" // TODO: Export to env variables
    private const val validityInMs = 36_000_00 * 100 // 10 hours
    private val algorithm = Algorithm.HMAC256(secret)
    lateinit var jwtIssuer: String
        private set
    lateinit var jwtAudience: String
        private set
    lateinit var jwtRealm: String
        private set

    fun init(environment: ApplicationEnvironment) {
        this.jwtIssuer = environment.config.property("jwt.domain").getString()
        this.jwtAudience = environment.config.property("jwt.audience").getString()
        this.jwtRealm = environment.config.property("jwt.realm").getString()
    }

    fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim("id", user.id)
        // .withArrayClaim("countries", user.countries.toTypedArray())
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    fun checkToken(token: String): Boolean {
        return false
    }

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}