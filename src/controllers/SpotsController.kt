package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import dev.remylavergne.spotfinder.services.SpotsService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.spotsController() {

    val spotsService: SpotsService = get()

    post("/spot/new") {
        call.getResponseObject<SpotCreationDto>()?.let {
            // Receive new spot informations
            val response = spotsService.createNewSpot(it)
            call.respondText(response)
        } ?: call.respond("Error")
    }
}
