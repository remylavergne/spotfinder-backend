package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import dev.remylavergne.spotfinder.controllers.dto.NewAccountDto
import dev.remylavergne.spotfinder.data.JWTTool
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
    val pictures: List<String> = listOf(),
    var token: String? = null
) {

    companion object {
        fun create(username: String, password: String): User {
            val id = UUID.randomUUID().toString().split("-")[0]
            val idUser = "$username#$id"
            val passwordHash = PasswordTools.generateHash(password)

            return User(
                id = idUser,
                createdAt = System.currentTimeMillis(),
                lastConnexion = System.currentTimeMillis(),
                username = username,
                password = passwordHash
            )
        }
    }

    fun toNewAccountDto(clearPassword: String, token: String): NewAccountDto {
        return NewAccountDto(id, username, clearPassword, token)
    }
}