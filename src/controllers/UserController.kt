package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.*
import dev.remylavergne.spotfinder.services.UserService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

// TODO: Exception class for each Router
fun Route.usersController() {

    val userService: UserService = get()

    authenticate {
        post("/user") {
            call.receive<String>().let {
                userService.getUser(it, null)?.let { response ->
                    call.respondText(
                        text = response,
                        status = HttpStatusCode.OK,
                        contentType = ContentType.Text.Plain
                    )
                } ?: call.respond(HttpStatusCode.NotFound, "This user doesn't exist")
            }
        }

        post("/user/update-password") {
            call.getResponseObject<UpdatePasswordDto>()?.let {
                val success: Boolean = userService.updatePassword(it)

                if (success) {
                    call.respondText(
                        text = "Password updated",
                        status = HttpStatusCode.OK,
                        contentType = ContentType.Text.Plain
                    )
                } else {
                    call.respondText(
                        text = "Error during password update",
                        status = HttpStatusCode.BadRequest,
                        contentType = ContentType.Text.Plain
                    )
                }
            } ?: call.respond(HttpStatusCode.NotFound, "Unable to update user password")
        }

        post("/user/comments") { // TODO: Refactor comme pour les photos
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val comments: String = userService.getComments(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = comments,
                    status = HttpStatusCode.OK
                )
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post("/user/pictures") {
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val pictures: String = userService.getPictures(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = pictures,
                    status = HttpStatusCode.OK
                )
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post("/user/pending-pictures") {
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val pictures: String = userService.getPendingPictures(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = pictures,
                    status = HttpStatusCode.OK
                )
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post("/user/spots") {
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val spots: String = userService.getSpots(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = spots,
                    status = HttpStatusCode.OK
                )
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post("/user/pending-spots") {
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val spots: String = userService.getPendingSpots(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = spots,
                    status = HttpStatusCode.OK
                )
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        put("/user/update-profile") {
            call.getResponseObject<UpdateUserProfile>()?.let {
                val response: String? = userService.updateProfile(it)

                if (response == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    call.respondText(
                        contentType = ContentType.Application.Json,
                        text = response,
                        status = HttpStatusCode.OK
                    )
                }

            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post("/user/statistics") {
            call.getResponseObject<SearchWithPaginationDto>()?.let {
                val response: String = userService.getUserStatictics(it)

                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = response,
                    status = HttpStatusCode.OK
                )

            } ?: call.respond(HttpStatusCode.BadRequest)
        }
    }

    post("/user/create") {
        call.getResponseObject<CreateAccountDto>()?.let {
            val user = userService.createUser(it)

            if (user == null) {
                call.respondText(
                    text = "User not created",
                    status = HttpStatusCode.NotFound,
                    contentType = ContentType.Text.Plain
                )
            } else {
                call.respondText(
                    text = user,
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }

    post("/user/retrieve-account") {
        call.getResponseObject<RetrieveAccountDto>()?.let {
            val user: String? = userService.retrieveAccount(it)

            if (user == null) {
                call.respondText(
                    text = "Can't retrieve account",
                    status = HttpStatusCode.NotFound,
                    contentType = ContentType.Text.Plain
                )
            } else {
                call.respondText(
                    text = user,
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            }

        } ?: call.respond(HttpStatusCode.BadRequest)
    }

    post("/user/reset-password") {
        call.getResponseObject<ResetPasswordDto>()?.let { data ->
            val success: Boolean = userService.resetPassword(data)

            if (success) {
                call.respondText(
                    text = "User found. Email will be sent quickly",
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            } else {
                call.respondText(
                    text = "User not found",
                    status = HttpStatusCode.NotFound,
                    contentType = ContentType.Text.Plain
                )
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }

    post("/user/reset-password-token-verification") {
        call.getResponseObject<ResetPasswordTokenDto>()?.let { token ->
            val success: Boolean = userService.resetPasswordCheckToken(token)

            if (success) {
                call.respondText(
                    text = success.toString(),
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            } else {
                call.respondText(
                    text = success.toString(),
                    status = HttpStatusCode.NotFound,
                    contentType = ContentType.Text.Plain
                )
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}
