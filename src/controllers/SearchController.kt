package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.SearchDto
import dev.remylavergne.spotfinder.services.SearchService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.searchController() {

    val searchDto: SearchService = get()

    post("/search") {
        call.getResponseObject<SearchDto>()?.let { query: SearchDto ->
            val data = searchDto.searchSpots(query)

            call.respondText(
                contentType = ContentType.Application.Json,
                text = data,
                status = HttpStatusCode.OK
            )
        } ?: call.respondText(
            text = "Error during search",
            status = HttpStatusCode.NotFound,
            contentType = ContentType.Text.Plain
        )
    }
}