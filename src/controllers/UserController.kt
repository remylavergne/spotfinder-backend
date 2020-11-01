package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.get

fun Route.usersController() {

    val userService: UserService = get()

    post("/user/log/{id}") {
        userService.logUserConnection(call.parameters)
        call.respondText(
            text = "User connection logs updated",
            status = HttpStatusCode.OK,
            contentType = ContentType.Text.Plain
        )
    }
}