package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.data.models.User
import dev.remylavergne.spotfinder.services.UserService
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

    get("/user") {
        val queryParams: Parameters = call.request.queryParameters
        val username = queryParams["username"]
        val id = queryParams["id"]
        // Check validity
        if (username == null && id == null) {
            throw Exception("Missing username or id")
        }
        // Process call
        userService.getUser(id, username)?.let { response ->
            call.respondText(
                text = response,
                status = HttpStatusCode.OK,
                contentType = ContentType.Text.Plain
            )
        } ?: call.respond(HttpStatusCode.NotFound, "This user doesn't exist")
    }

    post("/user/create") {
        val username = call.receive<String>()
        if (username.isEmpty()) {
            throw Exception("Username must not be empty")
        }

        userService.createUser(username).let { success: Boolean ->
            if (success) {
                call.respondText(
                    text = "User created",
                    status = HttpStatusCode.OK,
                    contentType = ContentType.Text.Plain
                )
            } else {
                call.respondText(
                    text = "User not created",
                    status = HttpStatusCode.InternalServerError,
                    contentType = ContentType.Text.Plain
                )
            }
        }
    }

    post("/user/log/{id}") {
        userService.logUserConnection(call.parameters)
        call.respondText(
            text = "User connection logs updated",
            status = HttpStatusCode.OK,
            contentType = ContentType.Text.Plain
        )
    }
}