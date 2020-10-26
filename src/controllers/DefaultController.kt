package dev.remylavergne.spotfinder.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.defaultController() {

    get("/") {
        call.respondText("Spotfinder Backend", status = HttpStatusCode.OK)
    }
}