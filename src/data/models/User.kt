package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val createdAt: Long,
    var lastConnexion: Long,
    val username: String,
    val spots: List<String> = listOf(),
    val pictures: List<String> = listOf()
) {
    companion object {
        fun create(username: String): User {
            val id = UUID.randomUUID().toString().split("-")[0]
            val idUser = "$username#$id"
            return User(
                id = idUser,
                createdAt = System.currentTimeMillis(),
                lastConnexion = System.currentTimeMillis(),
                username = username
            )
        }
    }
}