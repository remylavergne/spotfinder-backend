package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.data.models.User

interface EmailService {
 fun resetPassword(user: User, url: String): Boolean
}