package dev.remylavergne.spotfinder.controllers

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.get

fun Route.metricsController() {

    val registry = get<PrometheusMeterRegistry>()

    get("/metrics") {
        call.respondText {
            registry.scrape()
        }
    }
}