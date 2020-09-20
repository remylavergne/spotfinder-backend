package dev.remylavergne.spotfinder.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun routingTest(route: Route) {

    route.get("/michel") {
        call.respondText("HELLO FROM MICHEL!", contentType = ContentType.Text.Plain)
    }
}