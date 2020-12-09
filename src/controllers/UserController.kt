package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.RetrieveAccountDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.controllers.dto.SearchWithPaginationDto
import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.services.CommentsService
import dev.remylavergne.spotfinder.services.UserService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.call
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.get
import org.koin.ktor.ext.get

// TODO: Exception class for each Router
fun Route.usersController() {

    val userService: UserService = get()
    val commentsService: CommentsService = get()

    post("/user") {
        call.receive<String>()?.let {
            userService.getUser(it, null)?.let { response ->
                call.respondText(
                    text = response,
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            } ?: call.respond(HttpStatusCode.NotFound, "This user doesn't exist")
        } ?: call.respond(HttpStatusCode.NotFound, "This user doesn't exist")
    }

    post("/user/create") {
        val username = call.receive<String>()
        if (username.isEmpty()) {
            throw Exception("Username must not be empty")
        }

        val user = userService.createUser(username)

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

    post("/user/log/{id}") {
        userService.logUserConnection(call.parameters)
        call.respondText(
            text = "User connection logs updated",
            status = HttpStatusCode.OK,
            contentType = ContentType.Text.Plain
        )
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
}