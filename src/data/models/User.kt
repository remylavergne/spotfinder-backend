package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import dev.remylavergne.spotfinder.utils.PasswordTools
import java.util.*

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val createdAt: Long,
    var lastConnexion: Long,
    val username: String,
    val password: String?,
    val pictureId: String? = null,
    val spots: List<String> = listOf(),
    val pictures: List<String> = listOf()
) {
    companion object {
        fun create(username: String): User {
            val id = UUID.randomUUID().toString().split("-")[0]
            val idUser = "$username#$id"

            val generatedPassword = PasswordTools.generatePassword()
            val passwordHash = PasswordTools.generateHash(generatedPassword)

            return User(
                id = idUser,
                createdAt = System.currentTimeMillis(),
                lastConnexion = System.currentTimeMillis(),
                username = username,
                password = passwordHash
            )
        }
    }
}